package br.com.cafebinario.teseu.infrastruct.database.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpHeaders;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "headers")
@Api("HTTP-HEADERS")
public interface HttpHeadersRepository extends PagingAndSortingRepository<HttpHeaders, Long>, QueryByExampleExecutor<HttpHeaders> {

}
