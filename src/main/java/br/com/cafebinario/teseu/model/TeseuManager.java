package br.com.cafebinario.teseu.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cafebinario.teseu.api.ExecutionStatus;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@AllArgsConstructor
public final class TeseuManager {

	private final Map<String, String> tesseuRequestContext = Collections.synchronizedMap(new HashMap<>());

	private final String ordersName;
	private final TeseuRunMode teseuRunMode;
	private final TeseuInvoker teseuInvoker;
	private final TeseuParse<Path> teseuFileParse;
	private final TeseuParse<String> teseuDBparse;
	
	public ExecutionStatus execute() {

		try {
			
			if(teseuRunMode == TeseuRunMode.File) {
				execute(teseuFileParse, Paths.get(ordersName), teseuInvoker);
			}else {
				execute(teseuDBparse, ordersName, teseuInvoker);
			}
			
			return ExecutionStatus.Success;
			
		}catch (Exception e) {
			
			log.error("m=execute, ordersName={}, teseuRunMode={}", ordersName, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}
	
	private <T> void execute(final TeseuParse<T> teseuParse, final T ordersFileName, final TeseuInvoker teseuInvoker, final String... args) throws Exception {
		
		if(!tesseuRequestContext.isEmpty()) {
			throw new RuntimeException("there cannot be 2 (two) Theseus!");
		}
		
		final List<T> list = teseuParse.list(ordersFileName);
		
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
}
