package br.com.cafebinario.teseu.model.file;

import static br.com.cafebinario.teseu.model.TeseuConstants.BODY;
import static br.com.cafebinario.teseu.model.TeseuConstants.EMPTY;
import static br.com.cafebinario.teseu.model.TeseuConstants.FILENAME_KEY;
import static br.com.cafebinario.teseu.model.TeseuConstants.HEADERS;
import static br.com.cafebinario.teseu.model.TeseuConstants.ITEM_DECLARATION;
import static br.com.cafebinario.teseu.model.TeseuConstants.METHOD;
import static br.com.cafebinario.teseu.model.TeseuConstants.OUTPUT_FILE_EXTENSION;
import static br.com.cafebinario.teseu.model.TeseuConstants.PATH_SEPARATOR;
import static br.com.cafebinario.teseu.model.TeseuConstants.REGEX_FILE_SEPARATOR;
import static br.com.cafebinario.teseu.model.TeseuConstants.REGEX_SPACE;
import static br.com.cafebinario.teseu.model.TeseuConstants.SEPARATOR;
import static br.com.cafebinario.teseu.model.TeseuConstants.TESSEU_CONTEXT_FILE_NAME;
import static br.com.cafebinario.teseu.model.TeseuConstants.URI;
import static br.com.cafebinario.teseu.model.TeseuConstants.VAR_SEPARATOR;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.model.TeseuBinder;
import br.com.cafebinario.teseu.model.TeseuConstants;
import lombok.SneakyThrows;

@Service("teseuFileParse")
class TeseuFileParse implements TeseuParse<Path>{
	
	@Autowired
	private TeseuBinder teseuBinder;

	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public Map<String, String> read(final Path dirName, final Path inputSource, Map<String, String> teseuRequestContext) {
		
		final Map<String, String> contextLines = readContext(dirName);
		
		teseuRequestContext.put(FILENAME_KEY, inputSource.toString().split(REGEX_FILE_SEPARATOR)[0]);
		teseuRequestContext.putAll(contextLines);
		
		final List<String> lines = Files.readAllLines(dirName.resolve(inputSource));
		
		final StringBuilder body = new StringBuilder();
		int lineNumber = 0;
		boolean isBody = false;
		for (final String line : lines) {
			
			if(lineNumber == 0) {
				
				final String[] keyValue = line.split(REGEX_SPACE);
				
				teseuRequestContext.put(METHOD, keyValue[0].trim());
				teseuRequestContext.put(URI, URI.contains(ITEM_DECLARATION) ? teseuBinder.bind(keyValue[1], teseuRequestContext) : keyValue[1].trim());
				
				lineNumber++;
			}else if(line.equals(EMPTY)){
				isBody = true;
			} else if(isBody) {
				body.append(line + "\r\n");
			} else {
				final String[] keyValue = line.split(VAR_SEPARATOR);
				
				if(keyValue[1].substring(0, 1).equals("$")) {
					teseuRequestContext.put(HEADERS + PATH_SEPARATOR + keyValue[0].trim(), teseuBinder.bind(teseuRequestContext.get(keyValue[1].substring(1).trim()), 
							teseuRequestContext));
				}else {
					teseuRequestContext.put(HEADERS + PATH_SEPARATOR + keyValue[0].trim(), teseuBinder.bind(keyValue[1].trim(), 
							teseuRequestContext));
				}
			}
		}
		
		teseuRequestContext.put(BODY, teseuBinder.bind(body.toString(), teseuRequestContext));
		
		teseuBinder.validation(teseuRequestContext);
		
		return teseuRequestContext;
	}
	
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public void write(final Path dirName, final Map<String, String> tesseuResponseContext) {
		
		final String httpStatus = tesseuResponseContext.get(tesseuResponseContext.get(FILENAME_KEY) + "." + TeseuConstants.HTTP_STATUS);
		final String responseHeaders = tesseuResponseContext.get(tesseuResponseContext.get(FILENAME_KEY) + "." + TeseuConstants.RESPONSE_HEADERS);
		final String responseBody = tesseuResponseContext.get(tesseuResponseContext.get(FILENAME_KEY) + "." + TeseuConstants.RESPONSE_BODY);
		
		final Path path = resolveOutputFileName(dirName, tesseuResponseContext);
		
		try(final BufferedWriter bufferedWriter = Files.newBufferedWriter(path)){
			bufferedWriter.write("status-code:".concat(httpStatus)
					 .concat("\r\n")
					 .concat("response-headers:")
					 .concat(responseHeaders)
					 .concat("\r\n")
					 .concat("response-body:")
					 .concat(responseBody)
					 .concat("\r\n"));
			
			bufferedWriter.flush();
		} 
	}

	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public List<Path> list(final Path dirName, final Path inputSource) {

		return Files
				.readAllLines(dirName.resolve(inputSource))
				.stream()
				.map(Paths::get)
				.collect(Collectors.toList());
	}
	
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	@SneakyThrows
	@Override
	public void write(final Path dirName, final Map<String, String> tesseuRequestContext, final Path inputSource, final Throwable t) {
		
		final Path path = resolveOutputFileName(dirName, tesseuRequestContext);
		
		try(final BufferedWriter bufferedWriter = Files.newBufferedWriter(path)){
			
			bufferedWriter.write("path:".concat(inputSource.toString())
					 .concat("\r\n")
					 .concat("error:")
					 .concat(t.toString())
					 .concat("\r\n"));
		}
	}
	
	@Override
	@SneakyThrows
	public List<String> readExpectedExpressions(final Path name, final Map<String, String> tesseuRequestContext){
		
		return Files
				.readAllLines(name.resolve(Paths.get(tesseuRequestContext.get(FILENAME_KEY)) + ".expected"));
	}
	
	private Path resolveOutputFileName(final Path dirName, final Map<String, String> tesseuResponseContext) {
		
		return dirName.resolve(Paths.get(tesseuResponseContext.get(FILENAME_KEY).concat(OUTPUT_FILE_EXTENSION)));
	}
	
	private Map<String, String> readContext(final Path dirName) throws IOException {
		
		return Files.readAllLines(dirName.resolve(TESSEU_CONTEXT_FILE_NAME))
				.stream()
				.map(line->{
					final String[] variable = line.split(SEPARATOR);
					return Pair.of(variable[0], variable[1]);
				}).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
	}
}
