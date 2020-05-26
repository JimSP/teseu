package br.com.cafebinario.teseu.model.file;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.cafebinario.tesel.model.swagger.Definition;
import br.com.cafebinario.tesel.model.swagger.Parameter;
import br.com.cafebinario.tesel.model.swagger.Request;
import br.com.cafebinario.tesel.model.swagger.SwaggerSchema;
import br.com.cafebinario.teseu.model.TeseuConstants;
import br.com.cafebinario.teseu.model.http.SwaggerSchemaHttpInvoker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("gen")
@Slf4j
public class TeseuRegressiveTestFileModeGenerator implements CommandLineRunner {

	@Value("${br.com.cafebinario.teseu.swagger.api:http://localhost:8080}")
	private String apiUrl;

	@Value("${br.com.cafebinario.teseu.swagger.basePath:/}")
	private String basePath;

	@Value("${br.com.cafebinario.teseu.swagger.apiDoc:api-doc}")
	private String apiDoc;

	@Value("${br.com.cafebinario.teseu.context.name:teseu-regressive-tests}")
	private String name;

	@Autowired
	private SwaggerSchemaHttpInvoker swaggerSchemaHttpInvoker;

	@Override
	public void run(String... args) throws Exception {
		generate(name);
	}

	public void generate(final String name) {

		final SwaggerSchema swaggerSchema = swaggerSchemaHttpInvoker.invoke(apiUrl + basePath + apiDoc);

		log.info("m=generate, url=" + apiUrl + swaggerSchema.getBasePath());
		log.info("m=generate, swagger=" + swaggerSchema.getSwagger());
		log.info("m=generate, info=" + swaggerSchema.getInfo());
		log.info("m=generate, schemes=" + Arrays.toString(swaggerSchema.getSchemes().toArray()));
		log.info("m=generate, tags=" + Arrays.toString(swaggerSchema.getTags().toArray()));

		createTeseuContext(name, apiUrl + swaggerSchema.getBasePath());

		swaggerSchema.getPaths().forEach((path, methods) -> {
			createRequestType(name, path, methods, swaggerSchema.getDefinitions());
		});
	}

	@SneakyThrows
	private void createTeseuContext(final String name, final String url) {

		final String content = String.format("host=%s", url);

		createFile(name, TeseuConstants.TESSEU_CONTEXT_FILE_NAME, content);
	}

	private void createRequestType(final String dir, final String path, final Map<String, Request> methods,
			final Map<String, Definition> definitions) {

		methods.forEach((method, request) -> {

			final StringBuilder content = new StringBuilder();

			String pathParameter = path;
			final StringBuilder queryParams = new StringBuilder();
			final StringBuilder headers = new StringBuilder();
			final StringBuilder body = new StringBuilder();

			headers.append("accept:application/json\r\n");
			headers.append("Content-Type:application/json\r\n");

			for (final Parameter parameter : request.getParameters()) {

				switch (parameter.getIn()) {

				case "path":
					pathParameter = pathParameter.replace("{" + parameter.getName() + "}", "$" + parameter.getName());
					break;

				case "query":

					if (queryParams.length() > 0) {
						queryParams.append("&" + parameter.getName() + "=" + "$" + parameter.getName());
					} else {
						queryParams.append(parameter.getName() + "=" + "$" + parameter.getName());
					}

					break;

				case "header":
					headers.append(parameter.getName() + "=" + "$" + parameter.getName() + "\r\n");
					break;

				case "body":
					//final StringBuilder buider = new StringBuilder();
					final String[] parts = parameter.getSchema().getRef().split("[/]");
					
					//convertToJson(parts[parts.length - 1], definitions, definitions, buider);
					
					body.append("$" + parts[parts.length-1]);
					break;

				default:
					break;
				}
			}

			content.append(String.format("%s %s\r\n", method.toUpperCase(), formmatPath(pathParameter, queryParams)));
			content.append(headers.toString() + "\r\n");
			content.append(body.toString() + "\r\n");

			final String[] parts = path.split("[/]");

			final String fileName = Arrays.asList(parts).stream()
					.filter(predicate -> StringUtils.isNoneBlank(predicate)).reduce((a, b) -> a.concat("-").concat(b))
					.get();

			createFile(name, fileName + ".request", content.toString());
		});
	}

	private String formmatPath(final String pathParameter, final StringBuilder queryParams) {

		return pathParameter.toString()
				+ (queryParams.toString().length() > 0 ? "?" + queryParams.toString() + "=$" + queryParams.toString()
						: "");
	}

	@SneakyThrows
	private void createFile(final String dir, final String fileName, final String content) {

		Files.write(Paths.get(dir).resolve(fileName), content.getBytes());
	}
}
