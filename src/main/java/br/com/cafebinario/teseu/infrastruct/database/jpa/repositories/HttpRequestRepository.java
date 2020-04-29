package br.com.cafebinario.teseu.infrastruct.database.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.HttpRequest;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "requests")
@Api("HTTP-REQUESTS")
public interface HttpRequestRepository
		extends PagingAndSortingRepository<HttpRequest, Long>, QueryByExampleExecutor<HttpRequest> {

}
