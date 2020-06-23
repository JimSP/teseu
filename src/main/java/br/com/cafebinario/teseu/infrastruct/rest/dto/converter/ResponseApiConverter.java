package br.com.cafebinario.teseu.infrastruct.rest.dto.converter;

import java.util.Collections;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpResponse;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpResponseHeaders;
import br.com.cafebinario.teseu.infrastruct.rest.dto.HeaderApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ResponseApi;
import br.com.cafebinario.teseu.infrastruct.utils.ObjectMapperUtils;

@Component
public class ResponseApiConverter {

	public ResponseApi convert(HttpResponse httpResponse) {

		if (httpResponse != null) {
			return ResponseApi.builder()
							.id(httpResponse.getId())
							.inputSource(httpResponse.getInputSource())
							.statusCode(httpResponse.getStatusCode()) 
							.body(httpResponse.getBody())
							.executionStatus(httpResponse.getExecutionStatus())
							.responseHeaders(convertHeaders(httpResponse.getHttpResponseHeaders()))
							.build();
		} else {
			return null;
		}
	}
  
	private Set<HeaderApi> convertHeaders(Set<HttpResponseHeaders> httpResponseHeaders) {
		if (httpResponseHeaders != null) {
			return ObjectMapperUtils.mapAll(httpResponseHeaders, HeaderApi.class);
		}
		return Collections.emptySet();
	}
}
