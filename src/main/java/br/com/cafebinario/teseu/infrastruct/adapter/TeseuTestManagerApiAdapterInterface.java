package br.com.cafebinario.teseu.infrastruct.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;

public interface TeseuTestManagerApiAdapterInterface {

	TeseuRegressiveTest save(TeseuRegressiveTest httpRequest);
	
	Page<TeseuRegressiveTest> getAll(Pageable pageable);
 	
	TeseuRegressiveTest getById(Long id);
 
	void delete(Long id);
	
}
