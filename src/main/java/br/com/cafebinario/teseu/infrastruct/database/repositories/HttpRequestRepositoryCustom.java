package br.com.cafebinario.teseu.infrastruct.database.repositories;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;

public interface HttpRequestRepositoryCustom {

	HttpRequest findOneWithLazyDependencies(Long id) throws Exception;
	
}
