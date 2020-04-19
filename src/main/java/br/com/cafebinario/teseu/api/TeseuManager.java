package br.com.cafebinario.teseu.api;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TeseuManager {

	private final static Map<String, String> tesseuRequestContext = Collections.synchronizedMap(new HashMap<>());
	
	public static void execute(final TeseuParse teseuParse, final TeseuInvoker teseuInvoker, final String... args) throws Exception {
		
		if(!tesseuRequestContext.isEmpty()) {
			throw new RuntimeException("there cannot be 2 (two) Theseus!");
		}
		
		final List<Path> list = teseuParse.list();
		
		for (final Path path : list) {
			
			teseuParse.read(path, tesseuRequestContext);
			
			try {
				
				final Map<String, String> tesseuResponseContext = teseuInvoker.execute(tesseuRequestContext, args);
				
				teseuParse.write(tesseuResponseContext);
				
				tesseuRequestContext.putAll(tesseuResponseContext);
			}catch (Throwable t) {
				
				teseuParse.write(tesseuRequestContext, path, t);
				
				throw Minotaur.of("Theseus was lost in the maze on the way [" + path + "!]", t);
			}
		}
		
		tesseuRequestContext.clear();
	}
	
	private TeseuManager() {
		
	}
}
