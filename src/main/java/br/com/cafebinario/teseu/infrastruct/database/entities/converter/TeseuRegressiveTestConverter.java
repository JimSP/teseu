package br.com.cafebinario.teseu.infrastruct.database.entities.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpRequest;
import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuExecutionOrder;
import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;
import br.com.cafebinario.teseu.infrastruct.database.repositories.HttpRequestRepository;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ExecutionOrder;
import br.com.cafebinario.teseu.infrastruct.rest.dto.RegressiveTest;

@Component
public class TeseuRegressiveTestConverter {

	@Autowired
	private HttpRequestRepository httpRequestRepository;
	
	public TeseuRegressiveTest convert(RegressiveTest regressiveTest) {

		return TeseuRegressiveTest.builder()
				.id(regressiveTest.getId())
				.name(regressiveTest.getName())
				.teseuExecutionOrders(convertExecutionOrders(regressiveTest.getSequenceOfApis()))				 
				.build();
	}

	private List<TeseuExecutionOrder> convertExecutionOrders(final List<ExecutionOrder> executionOrders) {
		if (executionOrders != null) {
			return executionOrders
					.stream()
					.map(execOrder -> {						
						HttpRequest httpRequest = httpRequestRepository.findById(execOrder.getId()).get();
						return new TeseuExecutionOrder(execOrder.getExecutionOrder(), httpRequest);
					})
					.collect(Collectors.toList());			
		}
		return Collections.emptyList();
	}
 
}
