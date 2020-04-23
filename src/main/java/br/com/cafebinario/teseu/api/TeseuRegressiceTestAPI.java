package br.com.cafebinario.teseu.api;

public interface TeseuRegressiceTestAPI {

	ExecutionStatus execute(final Long clientId, final Long contextId, final Long requestId);

}
