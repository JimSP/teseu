package br.com.cafebinario.teseu.api;

public class Minotaur extends Exception{

	private static final long serialVersionUID = 7660404106123057506L;
	
	public static Exception of(final String message, final Throwable t) {
		return new Minotaur(message, t);
	}

	private Minotaur(final String message, final Throwable t) {
		super(message, t);
	}
}
