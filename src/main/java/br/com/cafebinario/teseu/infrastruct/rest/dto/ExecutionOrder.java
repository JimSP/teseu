package br.com.cafebinario.teseu.infrastruct.rest.dto;

import javax.validation.constraints.NotNull;

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
@EqualsAndHashCode(callSuper = false, of = { "id" })
@ToString(callSuper = true)
public class ExecutionOrder {

	private Long id;
	
	@NotNull
	private Integer executionOrder;
	
	@NotNull
	private RequestApi requestApi;
	
	@NotNull
	private ResponseApi responseApi;
	
}
