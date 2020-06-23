package br.com.cafebinario.teseu.model;

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
public final class TeseuManagerApi<T> {

	private final Map<String, String> tesseuRequestContext = Collections.synchronizedMap(new HashMap<>());

	private final Long testId;
	private final T name;
	private final T ordersName; 
	private final TeseuInvoker teseuInvoker;
	private final TeseuParse<T> teseuParse;
	 
	private final TeseuExpressionExpectedProcessor tesuExpectedProcessor;
	
	public List<T> getApis() {

		return teseuParse.list(testId, name, ordersName);
	}

	public ExecutionStatus execute(final T requestName, final String... args) throws Exception {

		if (!tesseuRequestContext.isEmpty()) {
			throw new RuntimeException("there cannot be 2 (two) Theseus!");
		}

		teseuParse.read(name, requestName, tesseuRequestContext);

		try {

			final Map<String, String> tesseuResponseContext = teseuInvoker.execute(tesseuRequestContext, args);
			tesseuRequestContext.putAll(tesseuResponseContext);
 
			final List<String> expressions = teseuParse.readExpectedExpressions(name, tesseuRequestContext);

			for (final String expression : expressions) {

				final Boolean expressionResult = tesuExpectedProcessor.parseExpression(expression,
						tesseuRequestContext);

				if (!expressionResult) {
					throw Minotaur.of(requestName + ".request contains error in expression" + expression);
				}
			}
			
			teseuParse.write(testId, name, requestName, tesseuRequestContext, ExecutionStatus.Success);


		} catch (final Throwable t) {
			
			log.error("m=execute, requestName={}", requestName, t);
			
			teseuParse.write(name, tesseuRequestContext, requestName, t);

			throw Minotaur.of("Theseus was lost in the maze on the way [" + name + "/" + requestName + "!]", t);
		}

		tesseuRequestContext.clear();

		return ExecutionStatus.Success;

	}
}
