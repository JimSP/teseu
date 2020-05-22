package br.com.cafebinario.teseu.infrastruct.rest.dto.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuExecutionOrder;
import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ExecutionOrder;
import br.com.cafebinario.teseu.infrastruct.rest.dto.RegressiveTest;

@Component
public class RegressiveTestConverter {

	public RegressiveTest convert(TeseuRegressiveTest teseuRegressiveTest) {

		return RegressiveTest.builder()
				.id(teseuRegressiveTest.getId())
				.name(teseuRegressiveTest.getName())
				.sequenceOfApis(convertExecOrders(teseuRegressiveTest.getTeseuExecutionOrders())) 
				.build();
	}

	private List<ExecutionOrder> convertExecOrders(List<TeseuExecutionOrder> teseuExecutionsOrders) {
		 
		return teseuExecutionsOrders
				.stream()
				.map(exc ->  new ExecutionOrder(exc.getId(), exc.getExecutionOrder(), exc.getHttpRequest().getId()))
				.collect(Collectors.toList());			
		
	}
 
}
