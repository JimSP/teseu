package br.com.cafebinario.teseu.infrastruct.rest;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cafebinario.teseu.api.ExecutionStatus;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.api.TeseuRegressiceTestAPI;
import br.com.cafebinario.teseu.model.TeseuExpressionExpectedProcessor;
import br.com.cafebinario.teseu.model.TeseuManager;
import br.com.cafebinario.teseu.model.TeseuRunMode;
import lombok.extern.slf4j.Slf4j;

@RestController("teseu")
@Profile("web")
@Slf4j
public class TeseuRegressiveTestRestAPI implements TeseuRegressiceTestAPI {

	@Autowired
	private TeseuInvoker teseuInvoker;
	
	@Autowired
	@Qualifier("teseuFileParse")
	private TeseuParse<Path> teseuFileParse;
	
	@Autowired
	@Qualifier("teseuDBparse")
	private TeseuParse<String> teseuDBparse;
	
	@Autowired
	private TeseuExpressionExpectedProcessor tesuExpectedProcessor;
	
	@Override
	@PostMapping(path = "/{ordersName}/{teseuRunMode}")
	public ExecutionStatus execute(@PathVariable(name = "ordersName", required = true) final String ordersName, @PathVariable("teseuRunMode") final String teseuRunMode) {

		try {

			return TeseuManager
					.builder()
					.ordersName(ordersName)
					.teseuDBparse(teseuDBparse)
					.teseuFileParse(teseuFileParse)
					.teseuInvoker(teseuInvoker)
					.teseuRunMode(TeseuRunMode.valueOf(teseuRunMode))
					.tesuExpectedProcessor(tesuExpectedProcessor)
					.build()
					.execute();
			
		}catch (Exception e) {
			
			log.error("m=execute, ordersName={}, teseuRunMode={}", ordersName, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}
}
