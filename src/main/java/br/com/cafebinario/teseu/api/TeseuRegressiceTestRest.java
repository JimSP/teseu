package br.com.cafebinario.teseu.api;

import java.nio.file.Path;
import java.util.List;

import br.com.cafebinario.teseu.infrastruct.rest.dto.ExecutionApi;

public interface TeseuRegressiceTestRest {

	void execute(final Long testId);

	void execute(final String ordersName, final String teseuRunMode);
}
