package br.com.cafebinario.teseu.model;

import static br.com.cafebinario.teseu.model.TeseuConstants.BODY;
import static br.com.cafebinario.teseu.model.TeseuConstants.DIR_NAME;
import static br.com.cafebinario.teseu.model.TeseuConstants.EMPTY;
import static br.com.cafebinario.teseu.model.TeseuConstants.FILENAME_KEY;
import static br.com.cafebinario.teseu.model.TeseuConstants.HEADERS;
import static br.com.cafebinario.teseu.model.TeseuConstants.HOST;
import static br.com.cafebinario.teseu.model.TeseuConstants.ITEM_DECLARATION;
import static br.com.cafebinario.teseu.model.TeseuConstants.METHOD;
import static br.com.cafebinario.teseu.model.TeseuConstants.ORDERS_FILE_NAME;
import static br.com.cafebinario.teseu.model.TeseuConstants.OUTPUT_FILE_EXTENSION;
import static br.com.cafebinario.teseu.model.TeseuConstants.PATH_SEPARATOR;
import static br.com.cafebinario.teseu.model.TeseuConstants.REGEX_FILE_SEPARATOR;
import static br.com.cafebinario.teseu.model.TeseuConstants.REGEX_SPACE;
import static br.com.cafebinario.teseu.model.TeseuConstants.SEPARATOR;
import static br.com.cafebinario.teseu.model.TeseuConstants.TESSEU_CONTEXT_FILE_NAME;
import static br.com.cafebinario.teseu.model.TeseuConstants.URI;
import static br.com.cafebinario.teseu.model.TeseuConstants.VAR_SEPARATOR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;
import br.com.cafebinario.teseu.api.TeseuParse;
import lombok.SneakyThrows;

@Service
class TeseuFileParse implements TeseuParse{

	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public void write(final Map<String, String> tesseuResponseContext) {
		
		final String responseBody = tesseuResponseContext.get("responseBody");
		final String responseHeaders = tesseuResponseContext.get("responseHeaders");
		final String httpStatus = tesseuResponseContext.get("httpStatus");
		
		final Path path = resolveOutputFileName(tesseuResponseContext);
		
		Files.newBufferedWriter(path)
					.write("status-code:".concat(httpStatus)
										 .concat("\r\n")
										 .concat("response-headers:")
										 .concat(responseHeaders)
										 .concat("\r\n")
										 .concat("response-body:")
										 .concat(responseBody)
										 .concat("\r\n"));
	}

	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public Map<String, String> read(final Path path, Map<String, String> tesseuRequestContext) {
		
		final Map<String, String> contextLines = readContext();
		
		tesseuRequestContext.put(FILENAME_KEY, path.toString().split(REGEX_FILE_SEPARATOR)[0]);
		tesseuRequestContext.putAll(contextLines);
		
		final List<String> lines = Files.readAllLines(Paths.get(DIR_NAME).resolve(path));
		
		final StringBuilder body = new StringBuilder();
		int lineNumber = 0;
		boolean isBody = false;
		for (final String line : lines) {
			
			if(lineNumber == 0) {
				
				final String[] keyValue = line.split(REGEX_SPACE);
				
				tesseuRequestContext.put(METHOD, keyValue[0].trim());
				tesseuRequestContext.put(URI, URI.contains(ITEM_DECLARATION) ? bindUrl(keyValue[1], tesseuRequestContext) : keyValue[1].trim());
				
				lineNumber++;
			}else if(line.equals(EMPTY)){
				isBody = true;
			} else if(isBody) {
				body.append(line + "\r\n");
			} else {
				final String[] keyValue = line.split(VAR_SEPARATOR);
				
				tesseuRequestContext.put(HEADERS + PATH_SEPARATOR + keyValue[0].trim(), keyValue[1].trim());
			}
		}
		
		tesseuRequestContext.put(BODY, body.toString());
		
		validation(tesseuRequestContext);
		
		return tesseuRequestContext;
	}

	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public List<Path> list() {

		return Files
				.readAllLines(Paths.get(DIR_NAME.concat(File.separator).concat(ORDERS_FILE_NAME)))
				.stream()
				.map(Paths::get)
				.collect(Collectors.toList());
	}
	
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public void write(final Map<String, String> tesseuRequestContext, final Path inputFile, final Throwable t) {
		
		final Path path = resolveOutputFileName(tesseuRequestContext);
		
		try(final BufferedWriter bufferedWriter = Files.newBufferedWriter(path)){
			
			bufferedWriter.write("path:".concat(inputFile.toString())
					 .concat("\r\n")
					 .concat("error:")
					 .concat(t.toString())
					 .concat("\r\n"));
		}
	}
	
	private Path resolveOutputFileName(final Map<String, String> tesseuResponseContext) {
		
		return Paths.get(DIR_NAME).resolve(Paths.get(tesseuResponseContext.get(FILENAME_KEY).concat(OUTPUT_FILE_EXTENSION)));
	}
	
	private Map<String, String> readContext() throws IOException {
		
		return Files.readAllLines(Paths.get(DIR_NAME.concat("/").concat(TESSEU_CONTEXT_FILE_NAME)))
				.stream()
				.map(line->{
					final String[] variable = line.split(SEPARATOR);
					return Pair.of(variable[0], variable[1]);
				}).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
	}
	
	private String bindUrl(final String url, final Map<String, String> tesseuRequestContext) {
		
		String newUrl = "";
		
		for (Map.Entry<String, String> entry : tesseuRequestContext.entrySet()) {
			newUrl = newUrl.replace("$" + entry.getKey(), url);
		}
		
		return newUrl.isEmpty() ? url : newUrl;
	}
	
	private void validation(final Map<String, String> map) {
		
		validation(map, HOST);
		validation(map, URI);
		validation(map, METHOD);
	}

	private void validation(final Map<String, String> map, final String key) {
		
		if(!map.containsKey(key))
			throw new IllegalArgumentException(key + " not declared in " + map.get(FILENAME_KEY));
	}
}
