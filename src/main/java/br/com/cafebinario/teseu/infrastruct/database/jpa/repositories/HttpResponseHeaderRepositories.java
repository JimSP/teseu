package br.com.cafebinario.teseu.infrastruct.database.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.HttpResponseHeaders;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "response-headers")
@Api("HTTP-RESPONSE-HEADERS")
public interface HttpResponseHeaderRepositories extends PagingAndSortingRepository<HttpResponseHeaders, Long>, QueryByExampleExecutor<HttpResponseHeaders> {

}
