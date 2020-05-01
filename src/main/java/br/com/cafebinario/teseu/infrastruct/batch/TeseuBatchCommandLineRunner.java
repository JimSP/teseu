package br.com.cafebinario.teseu.infrastruct.batch;

import java.nio.file.Path;
import java.util.Optional;

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
import br.com.cafebinario.teseu.model.TeseuSpelExpectedProcessor;
import br.com.cafebinario.teseu.model.TeseuManager;
import br.com.cafebinario.teseu.model.TeseuNotificationMode;
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
	
	@Value("${br.com.cafebinario.teseu.run-mode:File}")
	private String teseuRunMode;
	
	@Value("${br.com.cafebinario.teseu.notification-mode:None}")
	private String teseuNotificationModeString;
	
	@Autowired
	@Qualifier("teseuFileParse")
	private TeseuParse<Path> teseuFileParse;
	
	@Autowired
	@Qualifier("teseuDBparse")
	private TeseuParse<String> teseuDBparse;
	
	@Autowired
	private TeseuSpelExpectedProcessor teseuSpelExpectedProcessor;
	
	@Autowired
	@Qualifier("teseuEmailNotification")
	private TeseuNotification teseuEmailNotification;
	
	@Autowired
	@Qualifier("teseuSlackNotification")
	private TeseuNotification teseuSlackNotification;
	
	@Override
	@Log(logLevel = LogLevel.INFO, verboseMode = VerboseMode.ON)
	public void run(String... args) throws Exception {
		
		execute(ordersFileName, teseuRunMode);
	}

	@Override
	@SneakyThrows
	public ExecutionStatus execute(final String ordersName, final String teseuRunMode) {
		
		try {
			
			final TeseuNotificationMode teseuNotificationMode = TeseuNotificationMode.valueOf(teseuNotificationModeString);
			final Optional<TeseuNotification> teseuNotification = createNotification(ordersName, teseuNotificationMode);
			
			return TeseuManager
					.builder()
					.name(name)
					.ordersName(ordersName)
					.teseuDBparse(teseuDBparse)
					.teseuFileParse(teseuFileParse)
					.teseuInvoker(teseuInvoker)
					.teseuRunMode(TeseuRunMode.valueOf(teseuRunMode))
					.teseuExpectedProcessor(Optional.of(teseuSpelExpectedProcessor))
					.teseuNotification(teseuNotification)
					.build()
					.execute();
			
		}catch (Exception e) {
			
			log.error("m=execute, ordersName={}, teseuRunMode={}", ordersName, teseuRunMode, e);
			
			return ExecutionStatus.Error;
		}
	}

	private Optional<TeseuNotification> createNotification(final String ordersName, final TeseuNotificationMode teseuNotificationMode) {
		return teseuNotificationMode == TeseuNotificationMode.Email ? Optional.of(teseuEmailNotification)
				: teseuNotificationMode == TeseuNotificationMode.Slack ? Optional.of(teseuSlackNotification)
						: teseuNotificationMode == TeseuNotificationMode.None ? Optional.empty()
								
								: Optional.of(new TeseuNotification() {

									@Override
									public void sendReport(String name, Throwable t) {

										final Exception[] errors = new Exception[2];

										try {
											teseuEmailNotification.sendReport(ordersName, t);
										} catch (Exception e) {
											errors[0] = e;
										}

										try {
											teseuSlackNotification.sendReport(ordersName, t);
										} catch (Exception e) {
											errors[1] = e;

											if (errors[0] == null) {
												throw e;
											}
										}

										if (errors[0] != null) {
											final RuntimeException e0 = new RuntimeException(errors[0]);
											final RuntimeException e1 = new RuntimeException(errors[1]);

											if (e0 != null) {
												if (e1 != null) {
													e0.addSuppressed(e1);
												}
												throw e0;
											}
										}
									}
								});
	}
}
