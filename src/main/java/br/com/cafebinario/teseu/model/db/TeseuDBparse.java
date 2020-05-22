package br.com.cafebinario.teseu.model.db;

import static br.com.cafebinario.teseu.model.TeseuConstants.BODY;
import static br.com.cafebinario.teseu.model.TeseuConstants.FILENAME_KEY;
import static br.com.cafebinario.teseu.model.TeseuConstants.HOST;
import static br.com.cafebinario.teseu.model.TeseuConstants.METHOD;
import static br.com.cafebinario.teseu.model.TeseuConstants.URI;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpHeaders;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpResponse;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpResponseHeaders;
import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuExecutionOrder;
import br.com.cafebinario.teseu.infrastruct.database.repositories.HttpRequestRepository;
import br.com.cafebinario.teseu.infrastruct.database.repositories.HttpResponseRespository;
import br.com.cafebinario.teseu.infrastruct.database.repositories.TeseuExecutionOrderRepository;
import br.com.cafebinario.teseu.model.Minotaur;
import br.com.cafebinario.teseu.model.TeseuBinder;
import br.com.cafebinario.teseu.model.TeseuConstants;
import lombok.SneakyThrows;

@Service("teseuDBparse")
public class TeseuDBparse implements TeseuParse<String> {
	
	@Autowired
	private HttpRequestRepository httpRequestRepository;
	
	@Autowired
	private HttpResponseRespository httpResponseRespository;
	
	@Autowired
	private TeseuExecutionOrderRepository teseuExecutionOrderRepository;

	@Autowired
	private TeseuBinder teseuBinder;
	
	@Override
	@SneakyThrows
	public Map<String, String> read(final String name, final String requestName, final Map<String, String> teseuRequestContext) {
		
		final HttpRequest httpRequest = httpRequestRepository
											.findOne(Example.of(HttpRequest.builder().name(requestName).build()))
											.orElseThrow(()-> Minotaur.of("httpRequest of " + requestName + " not found"));

		teseuRequestContext.put("inputSource", httpRequest.getTeseuContext().getName());
		
		toMap(httpRequest, teseuRequestContext);
		
		teseuBinder.validation(teseuRequestContext);
		
		return teseuRequestContext;
	}

	@Override
	@SneakyThrows
	public List<String> list(final String name, final String inputSource) {

		return StreamSupport.stream(teseuExecutionOrderRepository
				.findAll(Example.of(
						TeseuExecutionOrder
							.builder() 
							.build()), Sort.by(TeseuExecutionOrder.orderBy())).spliterator(), false)
				.map(TeseuExecutionOrder::getHttpRequest)
				.map(HttpRequest::getName)
				.collect(Collectors.toList());
	}

	@Override
	public void write(final String name, final Map<String, String> teseuResponseContext) {
		
		final String inputSource = teseuResponseContext.get("inputSource");
		final String responseBody = teseuResponseContext.get(TeseuConstants.RESPONSE_BODY);
		final String responseHeaders = teseuResponseContext.get(TeseuConstants.RESPONSE_HEADERS);
		final String httpStatus = teseuResponseContext.get(TeseuConstants.HTTP_STATUS);
		
		final HttpResponse httpResponse = HttpResponse
												.builder()
												.inputSource(inputSource)
												.body(responseBody)
												.statusCode(Integer.valueOf(httpStatus))
												.build();
		
		final List<HttpResponseHeaders> httpResponseHeaders = toList(responseHeaders, httpResponse);
		httpResponse.setHttpResponseHeaders(httpResponseHeaders);
		
		httpResponseRespository.save(httpResponse);
	}

	@Override
	@SneakyThrows
	public void write(final String name, final Map<String, String> tesseuRequestContext, final String inputSource, final Throwable t) {
		
		throw t;
	}
	
	private Map<String, String> toMap(final HttpRequest httpRequest, final Map<String, String> tesseuRequestContext) {
		
		final Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
		
		map.put(FILENAME_KEY, httpRequest.getName());
		map.put(HOST, teseuBinder.bind(httpRequest.getHost(), tesseuRequestContext));
		map.put(METHOD, httpRequest.getMethod().name());
		
		final Map<String, String> headers = httpRequest
				.getHeaders()
				.stream()
				.collect(Collectors.toMap(HttpHeaders::getName, HttpHeaders::getValue));
		
		map.putAll(headers);
		
		map.put(URI, teseuBinder.bind(httpRequest.getPath(), tesseuRequestContext));
		map.put(BODY, teseuBinder.bind(httpRequest.getBody(), tesseuRequestContext));
		
		tesseuRequestContext.putAll(map);
		
		return tesseuRequestContext;
	}
	
	private List<HttpResponseHeaders> toList(final String responseHeaders, final HttpResponse httpResponse) {
		
		return Arrays
				.asList(responseHeaders.split("[,]"))
				.stream()
				.map(mapper->HttpResponseHeaders
								.builder()
								.httpResponse(httpResponse)
								.name(mapper.substring(0, mapper.indexOf("=")))
								.value(mapper.substring(mapper.indexOf("=") + 1, mapper.length()))
								.build())
				.collect(Collectors.toList());
	}
}
