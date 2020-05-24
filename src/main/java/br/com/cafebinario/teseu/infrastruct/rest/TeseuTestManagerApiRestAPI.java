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

import br.com.cafebinario.teseu.api.TeseuTestManagerApi;
import br.com.cafebinario.teseu.infrastruct.adapter.TeseuTestManagerApiAdapterInterface;
import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;
import br.com.cafebinario.teseu.infrastruct.database.entities.converter.TeseuRegressiveTestConverter;
import br.com.cafebinario.teseu.infrastruct.rest.dto.RegressiveTest;
import br.com.cafebinario.teseu.infrastruct.rest.dto.converter.RegressiveTestConverter;

@CrossOrigin
@RestController
@RequestMapping("/teseu-test-api")
@Profile("web")
public class TeseuTestManagerApiRestAPI implements TeseuTestManagerApi {

	@Autowired
	private TeseuTestManagerApiAdapterInterface teseuTestManagerApiAdapterInterface;

	@Autowired
	private TeseuRegressiveTestConverter teseuRegressiveTestConverter;
 
	@Autowired
	private RegressiveTestConverter regressiveTestConverter;
	
	@Override 
	public RegressiveTest save(@Valid final RegressiveTest regressiveTest) {
		final TeseuRegressiveTest teseuRegressiveTest = teseuRegressiveTestConverter.convert(regressiveTest);
		final TeseuRegressiveTest teseuRegressiveTestSaved = teseuTestManagerApiAdapterInterface.save(teseuRegressiveTest);
		return regressiveTestConverter.convert(teseuRegressiveTestSaved);
	}

	@Override
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody Page<RegressiveTest> getAll(final Pageable pageable) {

		final Page<TeseuRegressiveTest> entities = teseuTestManagerApiAdapterInterface.getAll(pageable);

		final Page<RegressiveTest> dtoPage = entities.map(new Function<TeseuRegressiveTest, RegressiveTest>() {
			@Override
			public RegressiveTest apply(TeseuRegressiveTest entity) {

				return regressiveTestConverter.convert(entity);
			}
		});

		return dtoPage;
	}

	@Override
	public RegressiveTest getById(final Long id) {
		final TeseuRegressiveTest teseuRegressiveTest = teseuTestManagerApiAdapterInterface.getById(id);
		return regressiveTestConverter.convert(teseuRegressiveTest);
	}

	@Override
	public void remover(Long id) {
		teseuTestManagerApiAdapterInterface.delete(id);
	}

	@Override
	public void update(@Valid final RegressiveTest regressiveTest) {
		final TeseuRegressiveTest teseuRegressiveTest = teseuRegressiveTestConverter.convert(regressiveTest);
		teseuTestManagerApiAdapterInterface.save(teseuRegressiveTest);
	}

}
