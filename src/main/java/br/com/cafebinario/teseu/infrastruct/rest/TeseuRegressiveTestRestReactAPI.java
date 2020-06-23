package br.com.cafebinario.teseu.infrastruct.rest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.api.TeseuRegressiceTestRest;
import br.com.cafebinario.teseu.model.TeseuExpressionExpectedProcessor;
import br.com.cafebinario.teseu.model.TeseuManagerApi;
import br.com.cafebinario.teseu.model.TeseuRunMode;
import lombok.extern.slf4j.Slf4j;
 
@CrossOrigin
@RestController
@RequestMapping("/teseu-react")
@Profile("web")
@Slf4j
public class TeseuRegressiveTestRestReactAPI implements TeseuRegressiceTestRest {

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
	@PostMapping
	public void execute(@RequestParam("ordersName") final String ordersName,
						@RequestParam("name") final String name) {

 
		try {
			 
			TeseuManagerApi<Path> managerApi = TeseuManagerApi
												   .<Path>builder() 
												   .name(Paths.get(name))
												   .ordersName(Paths.get(ordersName))
												   .teseuParse(teseuFileParse)
												   .teseuInvoker(teseuInvoker) 
												   .tesuExpectedProcessor(tesuExpectedProcessor)
												   .build();
			
			List<Path> reqsName = managerApi.getApis();
			
			for (Path requestName : reqsName) {
				managerApi.execute(requestName);
			}
 
		} catch (Exception e) {

			log.error("m=execute, ordersName={}, teseuRunMode={}", ordersName, TeseuRunMode.File, e);
		}
		 
	}

	@Override
	@PostMapping(path = "/{testId}")
	public void execute(@PathVariable(name = "testId", required = true) final Long testId) {
 
		try {

			TeseuManagerApi<String> managerApi = TeseuManagerApi
														.<String>builder()
														.testId(testId)
														.teseuParse(teseuDBparse)
														.teseuInvoker(teseuInvoker) 
														.tesuExpectedProcessor(tesuExpectedProcessor)
														.build();
			
			List<String> reqsName = managerApi.getApis();
			
			for (String requestName : reqsName) {
				managerApi.execute(requestName);
			}
 
		} catch (Exception e) {

			log.error("m=execute, testId={}, teseuRunMode={}", testId, TeseuRunMode.Rest, e);
 
		} 
	}
}
