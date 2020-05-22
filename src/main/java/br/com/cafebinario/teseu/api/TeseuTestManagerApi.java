package br.com.cafebinario.teseu.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.teseu.infrastruct.rest.dto.RegressiveTest;

public interface TeseuTestManagerApi {
 
	/***
	 * Gravação de novo teste gerenciado/executado pelo Teseu.
	 * 
	 * @param requestApi
	 * @return RequestApi
	 */
	@Log
	@PostMapping 
	@ResponseStatus(code = HttpStatus.CREATED)
	@ResponseBody RegressiveTest save(@RequestBody RegressiveTest regressiveTest);
	
	
	/**
	 * Alteração de teste gerenciado/executado pelo Teseu.
	 * 
	 * @param requestApi
	 */
	@Log
	@PutMapping
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	@ResponseBody void update(@RequestBody RegressiveTest regressiveTest);

	/***
	 * Consulta paginada aos testes.
	 * 
	 * @param nome
	 * @param pageable
	 * @return Page<TeseuRegressiveTest>
	 */
	@Log
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK) 
	@ResponseBody Page<RegressiveTest> getAll(Pageable pageable);

	/***
	 * acessa um teste gerenciado/executado pelo Teseu pelo seu id.
	 * 
	 * @param id
	 * @return TeseuRegressiveTest
	 */
	@GetMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody RegressiveTest getById(@PathVariable(name = "id", required = true) Long id);
 
	/***
	 * remove um teste gerenciado/executado pelo Teseu pelo id.
	 * 
	 * @param id
	 */
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	void remover(Long id);
}
