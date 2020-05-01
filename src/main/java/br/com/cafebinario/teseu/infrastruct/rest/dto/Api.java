package br.com.cafebinario.teseu.infrastruct.rest.dto;

import java.util.List;

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
public class Api {
 
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String httpMethod;
	
	@NotBlank
	private String host;
	
	@NotBlank
	private String path;
	
	private List<ParamApi> paramsApi;
	
	private List<HeaderApi> headersApi;
	
	private String body;
	
}
