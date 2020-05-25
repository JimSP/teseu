package br.com.cafebinario.teseu.infrastruct.rest;

import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cafebinario.teseu.api.TeseuManagerApi;
import br.com.cafebinario.teseu.infrastruct.adapter.TeseuManagerApiAdapterInterface;
import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;
import br.com.cafebinario.teseu.infrastruct.database.entities.converter.HttpRequestConverter;
import br.com.cafebinario.teseu.infrastruct.rest.dto.RequestApi;
import br.com.cafebinario.teseu.infrastruct.rest.dto.converter.RequestApiConverter;

@CrossOrigin
@RestController
@RequestMapping("/teseu-manager-api")
@Profile("web")
public class TeseuManagerApiRestAPI implements TeseuManagerApi {

	@Autowired
	private TeseuManagerApiAdapterInterface teseuManagerApiAdapterInterface;

	@Autowired
	private HttpRequestConverter httpRequestConverter;

	@Autowired
	private RequestApiConverter requestApiConverter;

	@Override 
	public RequestApi save(@Valid final RequestApi requestApi) {
		final HttpRequest httpRequest = httpRequestConverter.convert(requestApi);
		final HttpRequest httpRequestSaved = teseuManagerApiAdapterInterface.save(httpRequest);
		return requestApiConverter.convert(httpRequestSaved);
	}

	@Override
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody Page<RequestApi> getAll(final Pageable pageable) {

		final Page<HttpRequest> entities = teseuManagerApiAdapterInterface.getAll(pageable);

		final Page<RequestApi> dtoPage = entities.map(new Function<HttpRequest, RequestApi>() {
			@Override
			public RequestApi apply(HttpRequest entity) {

				return requestApiConverter.convert(entity);
			}
		});

		return dtoPage;
	}

	@Override
	public RequestApi getById(final Long id) {
		final HttpRequest httpRequest = teseuManagerApiAdapterInterface.getById(id);
		return requestApiConverter.convert(httpRequest);
	}

	@Override
	public void remover(Long id) {
		teseuManagerApiAdapterInterface.delete(id);
	}

	@Override
	public void update(@Valid final RequestApi requestApi) {
		final HttpRequest httpRequest = httpRequestConverter.convert(requestApi);
		teseuManagerApiAdapterInterface.save(httpRequest);
	}

}
