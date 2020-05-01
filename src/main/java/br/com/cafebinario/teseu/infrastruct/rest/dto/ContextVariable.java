package br.com.cafebinario.teseu.infrastruct.rest.dto;

import javax.validation.constraints.NotBlank;

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
public class ContextVariable {
 	
	private Long id;
	 
	private String name;
	
	@NotBlank
	private String variable;
	
	@NotBlank
	private String value;

}
