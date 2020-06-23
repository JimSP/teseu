package br.com.cafebinario.teseu.infrastruct.rest.dto.converter;

import java.util.Collections;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.ExpectedValues;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpHeaders;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpParams;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ExpectedValueApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.HeaderApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ParamApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.RequestApi;
import br.com.cafebinario.teseu.infrastruct.utils.ObjectMapperUtils;

@Component
public class RequestApiConverter {

	public RequestApi convert(HttpRequest httpRequest) {

		return RequestApi.builder()
							.id(httpRequest.getId())
							.name(httpRequest.getName())
							.httpMethod(httpRequest.getMethod().name())
							.host(httpRequest.getHost())
							.path(httpRequest.getPath())
							.body(httpRequest.getBody())
							.paramsApi(convertParams(httpRequest.getParams()))
							.headersApi(convertHeaders(httpRequest.getHeaders()))
							.expectedValuesApi(convertExpectedValues(httpRequest.getExpectedValues())).build();
	}

	private Set<HeaderApi> convertHeaders(Set<HttpHeaders> httpHeaders) {
		if (httpHeaders != null) {
			return ObjectMapperUtils.mapAll(httpHeaders, HeaderApi.class);
		}
		return Collections.emptySet();   
	}

	private Set<ParamApi> convertParams(Set<HttpParams> httpParams) {
		if (httpParams != null) {
			return ObjectMapperUtils.mapAll(httpParams, ParamApi.class);
		}
		return Collections.emptySet();  
	}

	private Set<ExpectedValueApi> convertExpectedValues(Set<ExpectedValues> expectedValues) {
		if (expectedValues != null) {
			return ObjectMapperUtils.mapAll(expectedValues, ExpectedValueApi.class);
		}
		return Collections.emptySet(); 
	}

}
