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
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.RESPONSE_BODY;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.RESPONSE_HEADERS;
import static br.com.cafebinario.teseu.infrastruct.TeseuConstants.URI;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BinaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import br.com.cafebinario.teseu.api.TeseuInvoker;
import lombok.SneakyThrows;

@Service
public class TeseuHttpInvoker implements TeseuInvoker{

	@Autowired
	private RestTemplate restTemplate;
	
	@SneakyThrows
	@Override
	public Map<String, String> execute(final Map<String, String> teseuRequestContext, final String... args) {
		
		final String url = HTTP_PROTOCOL + teseuRequestContext.get(HOST) + teseuRequestContext.get(URI);
		final HttpMethod method = toHttpMethod(teseuRequestContext.get(METHOD));
		final HttpHeaders headers = toHttpHeaders(teseuRequestContext);
		final String body = teseuRequestContext.get(BODY);
		
		final ResponseEntity<String> responseEntity = callHttp(teseuRequestContext, url, method, headers, body);
		
		final Map<String, String> tesseuResponseContext = Collections.synchronizedMap(new HashMap<>());
		
		toMap(RESPONSE_BODY, new ObjectMapper().readTree(responseEntity.getBody()), tesseuResponseContext);
		
		final HttpHeaders responseHeaders = responseEntity.getHeaders();
		
		final HttpStatus httpStatus = responseEntity.getStatusCode();
		

		tesseuResponseContext.put(RESPONSE_HEADERS, toResponseHeaders(responseHeaders));
		tesseuResponseContext.put(HTTP_STATUS, String.valueOf(httpStatus.value()));
		
		return tesseuResponseContext;
	}

	private ResponseEntity<String> callHttp(final Map<String, String> teseuRequestContext,
			final String url, final HttpMethod method, final HttpHeaders headers, final String body) {
		
		return restTemplate.exchange(url, method,
				new HttpEntity<>(body, headers), String.class, teseuRequestContext);
		
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
		
		headers
			.entrySet()
			.stream()
			.filter(entry->entry
								.getKey()
								.contains(HEADERS))
			.forEach((entry)->httpHeaders.addAll(
												entry.getKey().split("[" + HEADERS + ".]")[1], 
												Arrays.asList(entry.getValue().split(ITEM_SEPARATOR))));
		
		return httpHeaders;
	}
	
	@SneakyThrows
	public void toMap(final String key, final JsonNode parent, final Map<String, String> map) {
		
		final AtomicLong i = new AtomicLong(0);
		
		switch (parent.getNodeType()) {
		case ARRAY:
			final Iterator<JsonNode> itArray = parent.elements();
			while(itArray.hasNext()) {
				final JsonNode jsonObbj = itArray.next();
				toMap(key + "[" + i.getAndIncrement() + "]", jsonObbj, map);
			}
			break;

		case BINARY:
			map.put(key, Base64.getEncoder().encodeToString(parent.binaryValue()));
			break;

		case BOOLEAN:
			map.put(key, String.valueOf(parent.booleanValue()));
			break;

		case MISSING:
			map.put(key, "");
			break;

		case NULL:
			map.put(key, "null");
			break;

		case NUMBER:
			map.put(key, String.valueOf(parent.numberValue()));
			break;

		case OBJECT:
			final Iterator<Map.Entry<String, JsonNode>> itObj = parent.fields();
			while(itObj.hasNext()) {
				final Map.Entry<String, JsonNode> jsonObbj = itObj.next();
				toMap(key + "." + jsonObbj.getKey(), jsonObbj.getValue(), map);
			}
			break;

		case POJO:
			final Iterator<JsonNode> itPojo = parent.elements();
			while(itPojo.hasNext()) {
				final JsonNode childrenPojo = itPojo.next();
				toMap(key + "." + ((POJONode)childrenPojo).getPojo().getClass().getSimpleName(), childrenPojo, map);
			}
			break;

		case STRING:
			map.put(key, parent.textValue());
			break;
		default:
			break;
		}
	}
}
