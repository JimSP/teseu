package br.com.cafebinario.teseu.infrastruct.database.repositories.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;
import br.com.cafebinario.teseu.infrastruct.database.repositories.HttpRequestRepositoryCustom;
import br.com.cafebinario.teseu.model.Minotaur;

@Repository
public class HttpRequestRepositoryImpl implements HttpRequestRepositoryCustom {

	private static final String NAMED_GRAPH = "graph.httpRequest.headersAndParamsAndExpectedValues";

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public HttpRequest findOneWithLazyDependencies(Long id) throws Exception {

		EntityGraph<?> graph = entityManager.getEntityGraph(NAMED_GRAPH);

		Map<String, Object> properties = new HashMap<>();
		properties.put("javax.persistence.loadgraph", graph);

		HttpRequest httpRequest = entityManager.find(HttpRequest.class, id, properties);

		if (httpRequest == null) {
			throw Minotaur.of("HttpRequest of id " + id + " not found ");
		}

		return httpRequest;
	}

}
