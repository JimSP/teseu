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
public class Info implements Serializable {

	private static final long serialVersionUID = -1888390978033699088L;

	private String version;
	private String title;

}
