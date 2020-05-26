package br.com.cafebinario.teseu.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public final class Minotaur extends Exception {

	private static final long serialVersionUID = 7660404106123057506L;

	public static Exception of(final String message, final Throwable t) {
		return new Minotaur(message, t);
	}
	
	public static Exception of(final String message) {
		return new Minotaur(message, null);
	}
	
	private Minotaur(final String message, final Throwable t) {
		super(message, t);
	}
}
