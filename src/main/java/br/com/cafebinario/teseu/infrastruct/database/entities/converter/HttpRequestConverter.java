package br.com.cafebinario.teseu.infrastruct.database.entities.converter;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpHeaders;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpMethod;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpParams;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;
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
				.build();
	}

	private List<HttpHeaders> convertHeaders(final List<HeaderApi> headersApi) {
		if (headersApi != null) {
			return ObjectMapperUtils.mapAll(headersApi, HttpHeaders.class);
		}
		return Collections.emptyList();
	}

	private List<HttpParams> convertParams(final List<ParamApi> paramsApi) {
		if (paramsApi != null) {
			return ObjectMapperUtils.mapAll(paramsApi, HttpParams.class);			 
		}
		return Collections.emptyList();
	}

	private HttpMethod resolvHttpMethod(final String httpMethod) {
		return Enum.valueOf(HttpMethod.class, httpMethod);
	}
	
}
