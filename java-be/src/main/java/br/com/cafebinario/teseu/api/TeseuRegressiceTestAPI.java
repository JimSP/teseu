package br.com.cafebinario.teseu.api;

@FunctionalInterface
public interface TeseuRegressiceTestAPI {

	ExecutionStatus execute(final String ordersName, final String teseuRunMode);
}
