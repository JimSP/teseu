package br.com.cafebinario.teseu.infrastruct.database.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.entities.Client;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "clients")
@Api("CLIENTS")
public interface ClientRepository extends PagingAndSortingRepository<Client, Long>, QueryByExampleExecutor<Client> {

}
