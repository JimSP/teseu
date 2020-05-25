package br.com.cafebinario.teseu.exception;

public class TeseuManagerApiNotFoundException extends RuntimeException {
  
	private static final long serialVersionUID = 4809568031934988116L;
	
	private final Long id;

	public TeseuManagerApiNotFoundException(final Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
