package br.com.cafebinario.teseu.api;

public interface TeseuRegressiceTestRest {

	void execute(final Long testId);

	void execute(final String ordersName, final String teseuRunMode);
}
