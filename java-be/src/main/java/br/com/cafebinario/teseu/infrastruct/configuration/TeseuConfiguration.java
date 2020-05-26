package br.com.cafebinario.teseu.infrastruct.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.slack.api.Slack;

@Configuration
public class TeseuConfiguration {

	@Bean
	public RestTemplate restTemplate() {

		return new RestTemplate();
	}

	@Bean
	public Slack getSlack() {
		
		return Slack.getInstance();
	}
}
