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
public class Parameter implements Serializable{

	private static final long serialVersionUID = -5517222743185693223L;

	private String name;
	private String in;
	private String description;
	private Boolean required;
	private String type;
	private String format;
	private Schema schema;

}
