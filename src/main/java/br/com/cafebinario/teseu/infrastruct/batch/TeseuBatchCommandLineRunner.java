package br.com.cafebinario.teseu.infrastruct.batch;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import br.com.cafebinario.teseu.api.TeseuNotification;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.api.TeseuRegressiceTestAPI;
import br.com.cafebinario.teseu.infrastruct.notification.TeseuNotificationMode;
import br.com.cafebinario.teseu.model.TeseuManager;
import br.com.cafebinario.teseu.model.TeseuRunMode;
import br.com.cafebinario.teseu.model.TeseuSpelExpectedProcessor;
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
	
	@Value("${br.com.cafebinario.teseu.run-mode:File}")
	private String teseuRunMode;
	
	@Autowired
	@Qualifier("teseuFileParse")
	private TeseuParse<Path> teseuFileParse;
	
	@Autowired
	@Qualifier("teseuDBparse")
	private TeseuParse<String> teseuDBparse;
	
	@Value("${br.com.cafebinario.teseu.notification-mode:None}")
	private List<TeseuNotificationMode> teseuNotificationModes;
	
	@Autowired
	private List<TeseuNotification> teseuNotifications;
	
	@Autowired
	private TeseuSpelExpectedProcessor teseuSpelExpectedProcessor;
	
	@Override
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	public void run(String... args) throws Exception {
		
		execute(ordersFileName, teseuRunMode);
	}

	@Override
	@SneakyThrows
	public ExecutionStatus execute(final String ordersName, final String teseuRunMode) {
		
		try {
			
			if(TeseuRunMode.valueOf(teseuRunMode) == TeseuRunMode.File) {
				return TeseuManager
						.<Path>builder()
						.name(Paths.get(name))
						.ordersName(Paths.get(ordersName))
						.teseuparse(teseuFileParse)
						.teseuInvoker(teseuInvoker)
						.teseuExpectedProcessor(Optional.of(teseuSpelExpectedProcessor))
						.teseuNotifications(teseuNotifications
												.stream()
												.filter(teseuNotification->teseuNotificationModes
																				.stream()
																				.anyMatch(mode->teseuNotification
																									.getClass()
																									.isAssignableFrom(mode.getNotyficationType())))
												.collect(Collectors.toList()))
						.build()
						.execute();
			}else {
				return TeseuManager
						.<String>builder()
						.name(name)
						.ordersName(ordersName)
						.teseuparse(teseuDBparse)
						.teseuInvoker(teseuInvoker)
						.teseuExpectedProcessor(Optional.of(teseuSpelExpectedProcessor))
						.teseuNotifications(teseuNotifications
												.stream()
												.filter(teseuNotification->teseuNotificationModes
																				.stream()
																				.anyMatch(mode->teseuNotification
																									.getClass()
																									.isAssignableFrom(mode.getNotyficationType())))
												.collect(Collectors.toList()))
						.build()
						.execute();
			}
			
		}catch (Exception e) {
			
			log.error("m=execute, ordersName={}, teseuRunMode={}", ordersName, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}
}
