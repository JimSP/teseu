package br.com.cafebinario.teseu.infrastruct.batch;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;
import br.com.cafebinario.teseu.api.ExecutionStatus;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.api.TeseuRegressiceTestBatch;
import br.com.cafebinario.teseu.model.TeseuExpressionExpectedProcessor;
import br.com.cafebinario.teseu.model.TeseuManagerApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("batch")
@Slf4j
public class TeseuBatchCommandLineRunner implements CommandLineRunner, TeseuRegressiceTestBatch {

	@Autowired
	private TeseuInvoker teseuInvoker;
	
	@Value("${br.com.cafebinario.teseu.context.filename:execution-orders.teseu}")
	private String ordersFileName;
	
	@Value("${br.com.cafebinario.teseu.context.name:teseu-regressive-tests-copy}")
	private String name;
	
	@Value("${br.com.cafebinario.teseu.context.testId:-1}")
	private Long testId;
	
	@Autowired
	@Qualifier("teseuFileParse")
	private TeseuParse<Path> teseuFileParse;
	
	@Autowired
	@Qualifier("teseuDBparse")
	private TeseuParse<String> teseuDBparse;
	
	@Autowired
	private TeseuExpressionExpectedProcessor tesuExpectedProcessor;
	
	//@Value("${br.com.cafebinario.teseu.run-mode:Rest}")
	
	@Value("${br.com.cafebinario.teseu.run-mode:File}")	
	private String teseuRunMode;
	
	@Override
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	public void run(String... args) throws Exception {
		
		if (testId != null && testId != -1) {
			execute(testId);	
		} else {
			execute(ordersFileName, name);
		}
		
	}
 
	@Override
	@SneakyThrows
	public ExecutionStatus execute(final String ordersName, final String name) {
		
		try {
			
			TeseuManagerApi<Path> managerApi = TeseuManagerApi
													.<Path>builder()
													.testId(testId)
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
			
			return ExecutionStatus.Success;
			
		}catch (Exception e) {
			
			log.error("m=execute, name={}, ordersName={}, teseuRunMode={}", name, ordersName, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}

	@Override
	@SneakyThrows
	public ExecutionStatus execute(Long testId) {
		
		
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
			
			return ExecutionStatus.Success;
			
		}catch (Exception e) {
			
			log.error("m=execute, testId={}, teseuRunMode={}", testId, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}
 
}
