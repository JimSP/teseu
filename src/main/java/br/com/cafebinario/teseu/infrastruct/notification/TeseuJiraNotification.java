package br.com.cafebinario.teseu.infrastruct.notification;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.cafebinario.teseu.api.TeseuNotification;
import lombok.extern.slf4j.Slf4j;

@Component("teseuJiraNotification")
@Slf4j
public class TeseuJiraNotification implements TeseuNotification{

	@Value("${br.com.cafebinario.teseu.notification.jira.url}")
	private String jiraServer;
	
	@Value("${br.com.cafebinario.teseu.notification.jira.token}")
	private String token;
	
	@Value("${br.com.cafebinario.teseu.notification.jira.projectKey:TES}")
	private String projectKey;
	
	@Value("${br.com.cafebinario.teseu.notification.jira.issueTypeName:Task}")
	private String issueTypeName;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public void sendReport(final String name, final Throwable t) {
		
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");
		
		final JsonNodeFactory jnf = JsonNodeFactory.instance;
		final ObjectNode payload = jnf.objectNode();
		
		final ObjectNode fields = payload.putObject("fields");
		fields.put("summary", name);
		fields.putObject("issuetype").put("name", issueTypeName);
		fields.putObject("project").put("key", projectKey);
		fields.put("description", "[Teseu] " + t.toString().replaceAll("[\"]", ""));
		
		final RequestEntity<String> request = new RequestEntity<String>(payload.toPrettyString(), headers, HttpMethod.POST, URI.create(jiraServer + "/rest/api/2/issue"));
		
		final ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		
		if(!response.getStatusCode().is2xxSuccessful()) {
			log.error("m=sendReport, name={}, jira.response={}", response, t);
		}
	}
}
