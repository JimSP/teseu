package br.com.cafebinario.teseu.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpectedResult implements Serializable {

	private static final long serialVersionUID = -7151940344986223900L;

	private final Boolean sucess;
	private final String actual;
	private final String expected;

}