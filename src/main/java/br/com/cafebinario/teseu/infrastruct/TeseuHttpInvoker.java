package br.com.cafebinario.teseu.infrastruct;

import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.BODY;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.EMPTY;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.ENTRY_FORMAT;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.HEADERS;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.HOST;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.HTTP_PROTOCOL;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.HTTP_STATUS;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.ITEM_SEPARATOR;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.METHOD;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.PATH_SEPARATOR;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.RESPONSE_BODY;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.RESPONSE_HEADERS;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.URI;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.cafebinario.teseu.api.TeseuInvoker;

@Service
public class TeseuHttpInvoker implements TeseuInvoker{

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public Map<String, String> execute(final Map<String, String> teseuRequestContext, final String... args) {
		
		final String url = HTTP_PROTOCOL + teseuRequestContext.get(HOST) + teseuRequestContext.get(URI);
		final HttpMethod method = toHttpMethod(teseuRequestContext.get(METHOD));
		final HttpHeaders headers = toHttpHeaders(teseuRequestContext);
		final String body = teseuRequestContext.get(BODY);
		
		final ResponseEntity<Map<String, String>> responseEntity = callHttp(teseuRequestContext, url, method, headers, body);
		
		final Map<String, String> tesseuResponseContext = Collections.synchronizedMap(new HashMap<>());
		
		final Map<String, String> responseBody = extractBody(responseEntity);
		
		final HttpHeaders responseHeaders = responseEntity.getHeaders();
		
		final HttpStatus httpStatus = responseEntity.getStatusCode();
		
		tesseuResponseContext.putAll(responseBody);
		tesseuResponseContext.put(RESPONSE_HEADERS, toResponseHeaders(responseHeaders));
		tesseuResponseContext.put(HTTP_STATUS, String.valueOf(httpStatus.value()));
		
		return tesseuResponseContext;
	}

	private Map<String, String> extractBody(final ResponseEntity<Map<String, String>> responseEntity) {
		return responseEntity.getBody()
																.entrySet()
																.stream()
																.map(entry->Pair.of(RESPONSE_BODY + PATH_SEPARATOR + entry.getKey(), entry.getValue()))
																.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
	}

	private ResponseEntity<Map<String, String>> callHttp(final Map<String, String> teseuRequestContext,
			final String url, final HttpMethod method, final HttpHeaders headers, final String body) {
		
		return restTemplate.exchange(url, method,
				new HttpEntity<>(body, headers), new ParameterizedTypeReference<Map<String, String>>() {
				}, teseuRequestContext);
		
	}

	private HttpMethod toHttpMethod(final String method) {
		
		return HttpMethod.resolve(method);
	}

	private String toResponseHeaders(final HttpHeaders responseHeaders) {

		return responseHeaders
					.entrySet()
					.stream()
					.map(mapper->String.format(ENTRY_FORMAT,
												mapper.getKey(),
												mapper.
													getValue()
													.stream()
													.reduce(stringValue())
													.get()))
					.reduce(stringValue())
					.orElse(EMPTY);
	}

	private BinaryOperator<String> stringValue() {
		
		return (a,b)->a.concat(ITEM_SEPARATOR).concat(b);
	}
	
	private HttpHeaders toHttpHeaders(final Map<String, String> headers) {
		
		final HttpHeaders httpHeaders = new HttpHeaders();
		
		headers.entrySet().stream().filter(entry->entry.getKey().contains(HEADERS)).forEach((entry)->httpHeaders.addAll(entry.getKey().split("[" + HEADERS + ".]")[1], Arrays.asList(entry.getValue().split(ITEM_SEPARATOR))));
		
		return httpHeaders;
	}
}
