package br.com.cafebinario.teseu.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.cafebinario.teseu.api.ExecutionStatus;
import br.com.cafebinario.teseu.api.TeseuExpectedProcessor;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuNotification;
import br.com.cafebinario.teseu.api.TeseuParse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@AllArgsConstructor
public final class TeseuManager<T> {

	private final Map<String, String> tesseuRequestContext = Collections.synchronizedMap(new HashMap<>());

	private final T name;
	private final T ordersName;
	private final TeseuInvoker teseuInvoker;
	private final TeseuParse<T> teseuparse;
	private final Optional<TeseuExpectedProcessor> teseuExpectedProcessor;
	private final List<TeseuNotification> teseuNotifications;
	
	public Boolean addTeseuNotification(final TeseuNotification teseuNotification) {
		
		if(teseuNotifications != null) {
			return teseuNotifications.add(teseuNotification);
		}
		
		return false;
	}
	
	public ExecutionStatus execute() {

		try {
			
			execute(teseuparse, name, ordersName, teseuInvoker);
			
			return ExecutionStatus.Success;
			
		}catch (Exception e) {
			
			log.error("m=execute, ordersName={}", ordersName, e);
			
			return ExecutionStatus.Error;
		}
	}
	
	private void execute(final TeseuParse<T> teseuParse, final T name, final T ordersFileName, final TeseuInvoker teseuInvoker) throws Exception {
		
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
					
					teseuExpectedProcessor
						.ifPresent(consumer->{
							parseExpression(requestName, expression, consumer);
						});
				}

			}catch (final Throwable t) {
				
				teseuParse.write(name, tesseuRequestContext, requestName, t);
				
				teseuNotifications.forEach(teseuNotification->teseuNotification.sendReport(name + "." + requestName, t));
				
				throw Minotaur.of("Theseus was lost in the maze on the way [" + name + "." + requestName + "!]", t);
			}
		}
		
		tesseuRequestContext.clear();
	}

	@SneakyThrows
	private void parseExpression(final T requestName, final String expression, final TeseuExpectedProcessor teseuExpectedProcessor) {
		final Boolean expressionResult = teseuExpectedProcessor.parseExpression(expression, tesseuRequestContext);
		
		if(!expressionResult) {
			throw Minotaur.of(requestName + ".request contains error in expression" + expression);
		}
	}
}
