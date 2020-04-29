package br.com.cafebinario.teseu.infrastruct.database.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuExecutionOrder;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "execution-orders")
@Api("EXECUTION-ORDERS")
public interface TeseuExecutionOrderRepository  extends PagingAndSortingRepository<TeseuExecutionOrder, Long>, QueryByExampleExecutor<TeseuExecutionOrder> {

}
