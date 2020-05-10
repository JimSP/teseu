package br.com.cafebinario.teseu.model.http;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.cafebinario.tesel.model.swagger.SwaggerSchema;

@Component
public class SwaggerSchemaHttpInvoker {

	@Autowired
	private RestTemplate restTemplate;

	public SwaggerSchema invoke(final String apiDocUrl) {

		return restTemplate.getForEntity(URI.create(apiDocUrl), SwaggerSchema.class).getBody();
	}
}
