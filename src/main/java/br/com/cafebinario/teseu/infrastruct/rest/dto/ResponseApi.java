package br.com.cafebinario.teseu.infrastruct.rest.dto;

import java.util.Set;

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
public class ResponseApi {
 
	private Long id;
 
	private String inputSource;
	 
	private Integer statusCode;
	 
	private String body;
 
	private Set<HeaderApi> responseHeaders;
 
	private Integer executionStatus;
 
	
}
