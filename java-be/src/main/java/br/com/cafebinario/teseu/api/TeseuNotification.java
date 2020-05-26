package br.com.cafebinario.teseu.api;

@FunctionalInterface
public interface TeseuNotification {

	public void sendReport(final String name, final Throwable t);
}
