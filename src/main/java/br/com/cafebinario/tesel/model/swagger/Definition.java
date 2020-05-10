package br.com.cafebinario.tesel.model.swagger;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Definition implements Serializable{

	private static final long serialVersionUID = 520549521381909951L;
	
	private String type;
	private Map<String, Definition> properties;
	private String format;
	private String[] required;
	private Definition items;
	private String description;
	
	@JsonProperty("$ref")
	private String ref;

	
}
