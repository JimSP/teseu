package br.com.cafebinario.teseu.api;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface TeseuParse {

	void write(final Map<String, String> tesseuResponseContext);
	
	Map<String, String> read(final Path path, final Map<String, String> tesseuRequestContext);
	
	List<Path> list();

	void write(final Map<String, String> tesseuRequestContext, final Path inputFile, final Throwable t);
}
