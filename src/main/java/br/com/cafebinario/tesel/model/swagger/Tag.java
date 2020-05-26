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
public class Tag implements Serializable {

	private static final long serialVersionUID = 3915583354801463763L;

	private String name;

}
