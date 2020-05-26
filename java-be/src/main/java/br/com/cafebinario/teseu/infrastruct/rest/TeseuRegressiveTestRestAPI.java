package br.com.cafebinario.teseu.infrastruct.rest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cafebinario.teseu.api.ExecutionStatus;
import br.com.cafebinario.teseu.api.TeseuExpectedProcessor;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuNotification;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.api.TeseuRegressiceTestAPI;
import br.com.cafebinario.teseu.infrastruct.notification.TeseuNotificationMode;
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
	private TeseuExpectedProcessor teseuExpectedProcessor;
	
	@Value("${br.com.cafebinario.teseu.notification-mode:None}")
	private List<TeseuNotificationMode> teseuNotificationModes;
	
	@Autowired
	private List<TeseuNotification> teseuNotifications;
	
	@Override
	@PostMapping(path = "/{ordersName}/{teseuRunMode}")
	public ExecutionStatus execute(@PathVariable(name = "ordersName", required = true) final String ordersName, @PathVariable("teseuRunMode") final String teseuRunMode) {

		try {

			if(TeseuRunMode.DataBase == TeseuRunMode.valueOf(teseuRunMode)) {
				return TeseuManager
						.<String>builder()
						.ordersName(ordersName)
						.teseuparse(teseuDBparse)
						.teseuInvoker(teseuInvoker)
						.teseuExpectedProcessor(Optional.of(teseuExpectedProcessor))
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
						.<Path>builder()
						.ordersName(Paths.get(ordersName))
						.teseuparse(teseuFileParse)
						.teseuInvoker(teseuInvoker)
						.teseuExpectedProcessor(Optional.of(teseuExpectedProcessor))
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
