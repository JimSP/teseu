package br.com.cafebinario.teseu.model.notification;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Attachment;

import br.com.cafebinario.teseu.api.TeseuNotification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("teseuSlackNotification")
@Slf4j
public class TeseuSlackNotification implements TeseuNotification{

	@Value("${br.com.cafebinario.teseu.notification.slack.channelId}")
	private String slackChannelId;
	
	@Value("${br.com.cafebinario.teseu.notification.slack.token:no-content}")
	private String slackToken;
	
	@Autowired
	private Slack slackClient;
	
	@Override
	@SneakyThrows
	public void sendReport(final String name, final Throwable t) {
		
		final MethodsClient methods = slackClient.methods(slackToken);

		final ChatPostMessageRequest chatPostMessageRequest = ChatPostMessageRequest
																	.builder()
																	.text(t.getMessage())
																	.attachments(Arrays.asList(Attachment
																								.builder()
																								.title(name)
																								.text(t.toString())
																								.build()))
																	.channel(slackChannelId)
																	.build();
		
		final ChatPostMessageResponse response = methods.chatPostMessage(chatPostMessageRequest);
		
		log.debug("m=sendReport, response={}", response);
	}

}
