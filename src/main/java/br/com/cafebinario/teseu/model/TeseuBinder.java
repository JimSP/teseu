package br.com.cafebinario.teseu.model;

import static br.com.cafebinario.teseu.model.TeseuConstants.FILENAME_KEY;
import static br.com.cafebinario.teseu.model.TeseuConstants.HOST;
import static br.com.cafebinario.teseu.model.TeseuConstants.METHOD;
import static br.com.cafebinario.teseu.model.TeseuConstants.URI;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TeseuBinder {

	public String bind(final String content, final Map<String, String> tesseuRequestContext) {
		
		String newContent = content;
		
		for (final Map.Entry<String, String> entry : tesseuRequestContext.entrySet()) {
			newContent = content.replace("$" + entry.getKey(), entry.getValue());
		}
		
		return newContent;
	}
	
	public void validation(final Map<String, String> map) {
		
		validation(map, HOST);
		validation(map, URI);
		validation(map, METHOD);
	}
	
	private void validation(final Map<String, String> map, final String key) {
		
		if(!map.containsKey(key))
			throw new IllegalArgumentException(key + " not declared in " + map.get(FILENAME_KEY));
	}
}
