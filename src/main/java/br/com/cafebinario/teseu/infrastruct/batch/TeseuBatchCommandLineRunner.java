package br.com.cafebinario.teseu.infrastruct.batch;

import java.nio.file.Path;

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
import br.com.cafebinario.teseu.api.TeseuRegressiceTestAPI;
import br.com.cafebinario.teseu.model.TeseuSpelExpectedProcessor;
import br.com.cafebinario.teseu.model.TeseuManager;
import br.com.cafebinario.teseu.model.TeseuRunMode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("batch")
@Slf4j
public class TeseuBatchCommandLineRunner implements CommandLineRunner, TeseuRegressiceTestAPI {

	@Autowired
	private TeseuInvoker teseuInvoker;
	
	@Value("${br.com.cafebinario.teseu.context.filename:execution-orders.teseu}")
	private String ordersFileName;
	
	@Value("${br.com.cafebinario.teseu.context.name:teseu-regressive-tests}")
	private String name;
	
	@Autowired
	@Qualifier("teseuFileParse")
	private TeseuParse<Path> teseuFileParse;
	
	@Autowired
	@Qualifier("teseuDBparse")
	private TeseuParse<String> teseuDBparse;
	
	@Autowired
	private TeseuSpelExpectedProcessor teseuSpelExpectedProcessor;
	
	@Value("${br.com.cafebinario.teseu.run-mode:File}")
	private String teseuRunMode;
	
	@Override
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	public void run(String... args) throws Exception {
		
		execute(ordersFileName, teseuRunMode);
	}

	@Override
	@SneakyThrows
	public ExecutionStatus execute(final String ordersName, final String teseuRunMode) {
		
		try {
			
			return TeseuManager
					.builder()
					.name(name)
					.ordersName(ordersName)
					.teseuDBparse(teseuDBparse)
					.teseuFileParse(teseuFileParse)
					.teseuInvoker(teseuInvoker)
					.teseuRunMode(TeseuRunMode.valueOf(teseuRunMode))
					.teseuExpectedProcessor(teseuSpelExpectedProcessor)
					.build()
					.execute();
			
		}catch (Exception e) {
			
			log.error("m=execute, ordersName={}, teseuRunMode={}", ordersName, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}
}
