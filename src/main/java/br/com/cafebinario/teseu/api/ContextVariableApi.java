package br.com.cafebinario.teseu.api;

import java.util.List;

import org.springframework.data.domain.Pageable;

import br.com.cafebinario.teseu.infrastruct.rest.dto.ContextVariable;

public interface ContextVariableApi {


	/***
	 * Gravação de nova variável de contexto.
	 * 
	 * @param variableContext
	 * @return ContextVariable
	 */
	ContextVariable save(ContextVariable contextVariable);
	
	
	/**
	 * Alteração de variável de contexto.
	 * 
	 * @param contextVariable
	 */
	void update(ContextVariable contextVariable);

	/***
	 * Consulta paginada às variáveis de contexto.
	 * 
	 * @param nome
	 * @param pageable
	 * @return List<ContextVariable>
	 */
	List<ContextVariable> getAll(Pageable pageable);

	/***
	 * acessa uma variável de contexto pelo seu id.
	 * 
	 * @param id
	 * @return ContextVariable
	 */
	ContextVariable getById(Long id);
 
	/***
	 * remove uma variável de contexto pelo seu id.
	 * 
	 * @param id
	 */
	void remover(Long id);
}
