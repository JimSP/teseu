package br.com.cafebinario.teseu.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;

public final class TeseuManager {

	private final static Map<String, String> tesseuRequestContext = Collections.synchronizedMap(new HashMap<>());
	
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	public static <T> void execute(final TeseuParse<T> teseuParse, final T inputSource, final TeseuInvoker teseuInvoker, final String... args) throws Exception {
		
		if(!tesseuRequestContext.isEmpty()) {
			throw new RuntimeException("there cannot be 2 (two) Theseus!");
		}
		
		final List<T> list = teseuParse.list(inputSource);
		
		for (final T name : list) {
			
			teseuParse.read(name, tesseuRequestContext);
			
			try {
				
				final Map<String, String> tesseuResponseContext = teseuInvoker.execute(tesseuRequestContext, args);
				tesseuRequestContext.putAll(tesseuResponseContext);
				
				teseuParse.write(tesseuRequestContext);
				
			}catch (Throwable t) {
				
				teseuParse.write(tesseuRequestContext, name, t);
				
				throw Minotaur.of("Theseus was lost in the maze on the way [" + name + "!]", t);
			}
		}
		
		tesseuRequestContext.clear();
	}
	
	private TeseuManager() {
		
	}
}
