package br.com.cafebinario.teseu.infrastruct.database.repositories.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;
import br.com.cafebinario.teseu.infrastruct.database.repositories.TeseuRegressiveTestRepositoryCustom;
import br.com.cafebinario.teseu.model.Minotaur;

@Repository
public class TeseuRegressiveTestRepositoryImpl implements TeseuRegressiveTestRepositoryCustom {

	private static final String NAMED_GRAPH = "graph.teseuRegressiveTest.executionsOrders";

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public TeseuRegressiveTest findOneWithLazyDependencies(Long id) throws Exception {

		EntityGraph<?> graph = entityManager.getEntityGraph(NAMED_GRAPH);

		Map<String, Object> properties = new HashMap<>();
		properties.put("javax.persistence.loadgraph", graph);

		TeseuRegressiveTest teseuRegressiveTest = entityManager.find(TeseuRegressiveTest.class, id, properties);

		if (teseuRegressiveTest == null) {
			throw Minotaur.of("TeseuRegressiveTest of id " + id + " not found ");
		}

		return teseuRegressiveTest;
	}

}
