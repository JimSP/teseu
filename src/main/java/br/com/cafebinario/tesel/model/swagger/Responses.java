package br.com.cafebinario.tesel.model.swagger;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Responses implements Serializable{

	private static final long serialVersionUID = 1823700130068137448L;

	private StatusCode statusCode;

}
