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
public class StatusCode implements Serializable{

	private static final long serialVersionUID = -8935933092565171064L;

	private String description;
	private Schema schema;
}