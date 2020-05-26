package br.com.cafebinario.teseu.infrastruct.notification;

import br.com.cafebinario.teseu.api.TeseuNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TeseuNotificationMode {

	Email(TeseuEmailNotification.class), Slack(TeseuSlackNotification.class), Jira(TeseuJiraNotification.class), None(TeseuNoneNotification.class);
	
	private final Class<? extends TeseuNotification> notyficationType;
}
