package br.com.cafebinario.teseu.infrastruct.rest.dto;

import br.com.cafebinario.teseu.api.ExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false, of = { "requestName" })
@ToString(callSuper = true)
public class ExecutionApi<T> {

	private T requestName;
	 
	private ExecutionStatus executionStatus;
	 	
}
