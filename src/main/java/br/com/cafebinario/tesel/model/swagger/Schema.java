package br.com.cafebinario.tesel.model.swagger;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schema implements Serializable{

	private static final long serialVersionUID = -6578956708094391967L;

	@JsonProperty("$ref")
	private String ref;

}
