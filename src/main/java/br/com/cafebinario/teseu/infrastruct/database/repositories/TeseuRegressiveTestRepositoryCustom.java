package br.com.cafebinario.teseu.infrastruct.database.repositories;

import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;

public interface TeseuRegressiveTestRepositoryCustom {

	TeseuRegressiveTest findOneWithLazyDependencies(Long id) throws Exception;
	
}
