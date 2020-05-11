package br.com.cafebinario.teseu.infrastruct.rest.dto.converter;

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
				.build();
	}

	private List<HeaderApi> convertHeaders(List<HttpHeaders> httpHeaders) {
		return ObjectMapperUtils.mapAll(httpHeaders, HeaderApi.class);
	}

	private List<ParamApi> convertParams(List<HttpParams> httpParams) {
		return ObjectMapperUtils.mapAll(httpParams, ParamApi.class);
	}
}
