package br.com.cafebinario.teseu.api;

import java.util.Map;

@FunctionalInterface
public interface TeseuExpectedProcessor {

	Boolean parseExpression(final String expressionString, final Map<String, String> tesseuRequestContext);
}
