package br.com.cafebinario.teseu.infrastruct.adapter;

import java.util.List;

import org.springframework.data.domain.Pageable;

import br.com.cafebinario.teseu.infrastruct.rest.dto.ContextVariable;

public interface ContextVariableAdapterInterface {

	ContextVariable save(ContextVariable contextVariable);
	
	List<ContextVariable> getAll(Pageable pageable);
 	
	ContextVariable getById(Long id);
	
}
