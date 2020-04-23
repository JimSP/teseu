package br.com.cafebinario.teseu.infrastruct.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cafebinario.teseu.api.ExecutionStatus;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.api.TeseuRegressiceTestAPI;
import br.com.cafebinario.teseu.model.TeseuManager;

@RestController("teseu")
@Profile("web")
public class TeseuRegressiveTestRestAPI implements TeseuRegressiceTestAPI{

	@Autowired
	private TeseuInvoker teseuInvoker;
	
	@Autowired
	@Qualifier("teseuDBparse")
	private TeseuParse<String> teseuParse;
	
	@Override
	@PostMapping(path = "/{clientId}/{contextId}/requestId")
	public ExecutionStatus execute(final Long clientId, final Long contextId, final Long requestId) {

		try {
			
			TeseuManager.execute(teseuParse, "", teseuInvoker);
			
			return ExecutionStatus.Success;
		}catch (Exception e) {
			return ExecutionStatus.Error;
		}
	}
}
