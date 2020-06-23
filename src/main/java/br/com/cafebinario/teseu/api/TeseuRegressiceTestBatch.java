package br.com.cafebinario.teseu.api;

public interface TeseuRegressiceTestBatch {

	ExecutionStatus execute(final Long testId);

	ExecutionStatus execute(final String ordersName, final String teseuRunMode);
}
