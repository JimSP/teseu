package br.com.cafebinario.teseu.infrastruct.database.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.HttpResponse;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "responses")
@Api("HTTP-RESPONSES")
public interface HttpResponseRespository extends PagingAndSortingRepository<HttpResponse, Long>, QueryByExampleExecutor<HttpResponse> {

}
