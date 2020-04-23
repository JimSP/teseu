package br.com.cafebinario.teseu.api;

import java.util.List;
import java.util.Map;

public interface TeseuParse<S> {

	void write(final Map<String, String> tesseuResponseContext);
	
	Map<String, String> read(final S inputSource, final Map<String, String> tesseuRequestContext);
	
	List<S> list(final S inputSource);

	void write(final Map<String, String> tesseuRequestContext, final S inputSource, final Throwable t);
}
