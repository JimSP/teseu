package br.com.cafebinario.teseu.exception;

public class ContextVariableNotFoundException extends RuntimeException {
 
	private static final long serialVersionUID = -6454643885221638761L;
	
	private final Long id;

	public ContextVariableNotFoundException(final Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
