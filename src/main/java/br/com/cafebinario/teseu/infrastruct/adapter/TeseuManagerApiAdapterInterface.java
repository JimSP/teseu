package br.com.cafebinario.teseu.infrastruct.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;

public interface TeseuManagerApiAdapterInterface {

	HttpRequest save(HttpRequest httpRequest);
	
	Page<HttpRequest> getAll(Pageable pageable);
 	
	HttpRequest getById(Long id);
 
	void delete(Long id);
	
}
