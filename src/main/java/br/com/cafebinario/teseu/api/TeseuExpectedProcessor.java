package br.com.cafebinario.teseu.api;

import java.util.Map;

import br.com.cafebinario.teseu.model.ExpectedResult;

@FunctionalInterface
public interface TeseuExpectedProcessor {

	ExpectedResult parseExpression(final String expressionString, final Map<String, String> tesseuRequestContext);
}
