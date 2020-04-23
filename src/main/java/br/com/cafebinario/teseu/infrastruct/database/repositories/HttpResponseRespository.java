package br.com.cafebinario.teseu.infrastruct.database.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpResponse;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "responses")
@Api("HTTP-RESPONSES")
public interface HttpResponseRespository extends PagingAndSortingRepository<HttpResponse, Long>, QueryByExampleExecutor<HttpResponse> {

}
