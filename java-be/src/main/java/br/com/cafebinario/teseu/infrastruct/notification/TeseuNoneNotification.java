package br.com.cafebinario.teseu.infrastruct.notification;

import org.springframework.stereotype.Component;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;
import br.com.cafebinario.teseu.api.TeseuNotification;

@Component("teseuNoneNotification")
public class TeseuNoneNotification implements TeseuNotification{

	@Override
	@Log(logLevel = LogLevel.DEBUG, verboseMode = VerboseMode.OFF)
	public void sendReport(final String name, final Throwable t) {

	}
}
