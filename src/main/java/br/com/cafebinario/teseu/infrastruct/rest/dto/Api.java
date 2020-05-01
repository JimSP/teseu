package br.com.cafebinario.teseu.infrastruct.rest.dto;

import java.util.List;

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
	private String name;
	private String httpMethod;
	private String host;
	private String path;
	private List<ParamApi> paramsApi;
	private List<HeaderApi> headersApi;
	private String body;
	
}
