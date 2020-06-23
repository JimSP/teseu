package br.com.cafebinario.teseu.model.db;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuContext;
import br.com.cafebinario.teseu.infrastruct.database.repositories.ClientRepository;
import br.com.cafebinario.teseu.model.Minotaur;
import lombok.SneakyThrows;

@Component
public class TeseuNameResolver {

	@Autowired
	private ClientRepository clientRepository;
	
	@SneakyThrows
	public String resolve(final Long clientId, final Long teseuContextId) {
		
		return getTeseuContext(clientId, teseuContextId)
				.map(teseuContext -> teseuContext.getName())
				.orElseThrow(()-> Minotaur.of("teseuContext of " + teseuContextId + " not found"));
	}

	
	@SneakyThrows
	private Optional<TeseuContext> getTeseuContext(final Long clientId, final Long teseuContextId) {
		
		return clientRepository.findById(clientId).orElseThrow(()-> Minotaur.of("client of " + clientId + " not found"))
					.getTeseuContexts()
					.stream()
					.filter(teseuContext -> teseuContext.getId().equals(teseuContextId))
					.findFirst();
	}
}
