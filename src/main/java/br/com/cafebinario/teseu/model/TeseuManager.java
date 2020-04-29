package br.com.cafebinario.teseu.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cafebinario.teseu.api.ExecutionStatus;
import br.com.cafebinario.teseu.api.TeseuExpectedProcessor;
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

	private final String name;
	private final String ordersName;
	private final TeseuRunMode teseuRunMode;
	private final TeseuInvoker teseuInvoker;
	private final TeseuParse<Path> teseuFileParse;
	private final TeseuParse<String> teseuDBparse;
	private final TeseuExpectedProcessor teseuExpectedProcessor;
	
	public ExecutionStatus execute() {

		try {
			
			if(teseuRunMode == TeseuRunMode.File) {
				execute(teseuFileParse, Paths.get(name), Paths.get(ordersName), teseuInvoker);
			}else {
				execute(teseuDBparse, name, ordersName, teseuInvoker);
			}
			
			return ExecutionStatus.Success;
			
		}catch (Exception e) {
			
			log.error("m=execute, ordersName={}, teseuRunMode={}", ordersName, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}
	
	private <T> void execute(final TeseuParse<T> teseuParse, final T name, final T ordersFileName, final TeseuInvoker teseuInvoker) throws Exception {
		
		if(!tesseuRequestContext.isEmpty()) {
			throw new RuntimeException("there cannot be 2 (two) Theseus!");
		}
		
		final List<T> list = teseuParse.list(name, ordersFileName);
		
		for (final T requestName : list) {
			
			teseuParse.read(name, requestName, tesseuRequestContext);
			
			try {
				
				final Map<String, String> tesseuResponseContext = teseuInvoker.execute(tesseuRequestContext);
				tesseuRequestContext.putAll(tesseuResponseContext);
				
				teseuParse.write(name, tesseuRequestContext);
				
				final List<String> expressions = teseuParse.readExpectedExpressions(name, tesseuRequestContext);
				
				for (final String expression : expressions) {
					
					final Boolean expressionResult = teseuExpectedProcessor.parseExpression(expression, tesseuRequestContext);
					
					if(!expressionResult) {
						throw Minotaur.of(requestName + ".request contains error in expression" + expression);
					}
				}

			}catch (final Throwable t) {
				
				teseuParse.write(name, tesseuRequestContext, requestName, t);
				
				throw Minotaur.of("Theseus was lost in the maze on the way [" + name + "/" + requestName + "!]", t);
			}
		}
		
		tesseuRequestContext.clear();
	}
}
