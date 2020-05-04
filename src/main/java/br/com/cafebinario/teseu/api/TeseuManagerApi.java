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
import br.com.cafebinario.teseu.infrastruct.rest.dto.RequestApi;

public interface TeseuManagerApi {
 
	/***
	 * Gravação de nova Api gerenciada/executada pelo Teseu.
	 * 
	 * @param requestApi
	 * @return RequestApi
	 */
	@Log
	@PostMapping 
	@ResponseStatus(code = HttpStatus.CREATED)
	@ResponseBody RequestApi save(@RequestBody RequestApi requestApi);
	
	
	/**
	 * Alteração de Api gerenciada/executada pelo Teseu.
	 * 
	 * @param requestApi
	 */
	@Log
	@PutMapping
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	@ResponseBody void update(@RequestBody RequestApi requestApi);

	/***
	 * Consulta paginada às apis de usuário.
	 * 
	 * @param nome
	 * @param pageable
	 * @return Page<RequestApi>
	 */
	@Log
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK) 
	@ResponseBody Page<RequestApi> getAll(Pageable pageable);

	/***
	 * acessa uma api gerenciada/executada pelo Teseu pelo seu id.
	 * 
	 * @param id
	 * @return RequestApi
	 */
	@GetMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody RequestApi getById(@PathVariable(name = "id", required = true) Long id);
 
	/***
	 * remove uma api gerenciada/executada pelo Teseu pelo id.
	 * 
	 * @param id
	 */
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	void remover(Long id);
}
