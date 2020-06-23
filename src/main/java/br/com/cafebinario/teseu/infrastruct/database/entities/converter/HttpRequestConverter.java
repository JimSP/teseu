package br.com.cafebinario.teseu.infrastruct.database.entities.converter;

import java.util.Collections;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.ExpectedValues;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpHeaders;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpMethod;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpParams;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ExpectedValueApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.HeaderApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ParamApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.RequestApi;
import br.com.cafebinario.teseu.infrastruct.utils.ObjectMapperUtils;

@Component
public class HttpRequestConverter {

	public HttpRequest convert(RequestApi requestApi) {

		return HttpRequest.builder()
				.id(requestApi.getId())
				.name(requestApi.getName())
				.method(resolvHttpMethod(requestApi.getHttpMethod()))
				.host(requestApi.getHost())
				.path(requestApi.getPath())
				.body(requestApi.getBody())
				.params(convertParams(requestApi.getParamsApi()))
				.headers(convertHeaders(requestApi.getHeadersApi()))
				.expectedValues(convertExpectedValues(requestApi.getExpectedValuesApi()))
				.build();
	}

	private Set<HttpHeaders> convertHeaders(final Set<HeaderApi> headersApi) {
		if (headersApi != null) { 
			return ObjectMapperUtils.mapAll(headersApi, HttpHeaders.class);
		}
		return Collections.emptySet();
	}

	private Set<HttpParams> convertParams(final Set<ParamApi> paramsApi) {
		if (paramsApi != null) {			 
			return ObjectMapperUtils.mapAll(paramsApi, HttpParams.class);			 
		}
		return Collections.emptySet();
	}

	private Set<ExpectedValues> convertExpectedValues(final Set<ExpectedValueApi> expectedValuesApi) {
		if (expectedValuesApi != null) {
			return ObjectMapperUtils.mapAll(expectedValuesApi, ExpectedValues.class);			 
		}
		return Collections.emptySet();
	}
	
	private HttpMethod resolvHttpMethod(final String httpMethod) {
		return Enum.valueOf(HttpMethod.class, httpMethod);
	}
	
}
