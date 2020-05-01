package br.com.cafebinario.teseu.infrastruct.rest.dto;

import java.util.LinkedHashSet;

import javax.validation.constraints.NotBlank;
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
public class RegressiveTest {

	private Long id;
	
	@NotBlank
	private String name;
	
	@NotNull
	private LinkedHashSet<Api> sequenceOfApis;
	
}
