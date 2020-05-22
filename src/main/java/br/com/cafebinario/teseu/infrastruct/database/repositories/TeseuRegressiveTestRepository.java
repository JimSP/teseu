package br.com.cafebinario.teseu.infrastruct.database.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;
import io.swagger.annotations.Api;

@RepositoryRestResource(path = "teseu-regressive-tests")
@Api("TESEU-REGRESSIVE_TESTS")
public interface TeseuRegressiveTestRepository
		extends PagingAndSortingRepository<TeseuRegressiveTest, Long>, QueryByExampleExecutor<TeseuRegressiveTest> {

}
