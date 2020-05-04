package br.com.cafebinario.teseu.infrastruct.database.entities.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpHeaders;
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
				.name(requestApi.getName())
				.host(requestApi.getHost())
				.path(requestApi.getPath())
				.body(requestApi.getBody())
				.params(convertParams(requestApi.getParamsApi()))
				.headers(convertHeaders(requestApi.getHeadersApi()))
				.build();
	}

	private List<HttpHeaders> convertHeaders(List<HeaderApi> headersApi) {
		return ObjectMapperUtils.mapAll(headersApi, HttpHeaders.class);
	}

	private List<HttpParams> convertParams(List<ParamApi> paramsApi) {
		return ObjectMapperUtils.mapAll(paramsApi, HttpParams.class);
	}

}
