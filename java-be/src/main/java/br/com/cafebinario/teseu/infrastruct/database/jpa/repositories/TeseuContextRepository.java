package br.com.cafebinario.teseu.infrastruct.database.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.TeseuContext;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "teseu-contexts")
@Api("TESEU-CONTEXTS")
public interface TeseuContextRepository
		extends PagingAndSortingRepository<TeseuContext, Long>, QueryByExampleExecutor<TeseuContext> {

}
