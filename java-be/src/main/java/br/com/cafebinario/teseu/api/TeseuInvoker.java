package br.com.cafebinario.teseu.api;

import java.util.Map;

public interface TeseuInvoker {

	Map<String, String> execute(final Map<String, String> tesseuRequestContext, final String... args);
}
