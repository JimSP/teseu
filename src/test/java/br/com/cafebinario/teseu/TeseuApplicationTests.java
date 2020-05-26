package br.com.cafebinario.teseu;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeseuApplicationTests {
	
	@Value("${br.com.cafebinario.teseu.notification.jira.username}")
	private String username;
	
	@Value("${br.com.cafebinario.teseu.notification.jira.tokenApi}")
	private String tokenApi;

	@Test
	void contextLoads() {
		final String basicAuthorizationToken = "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", username, tokenApi).getBytes());
		
		System.out.println(basicAuthorizationToken);
	}

}