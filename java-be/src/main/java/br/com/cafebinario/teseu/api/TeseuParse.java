package br.com.cafebinario.teseu.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface TeseuParse<S> {

	List<S> list(final S name, final S inputSource);
	
	Map<String, String> read(final S name, final S inputSource, final Map<String, String> tesseuRequestContext);

	void write(final S name, final Map<String, String> tesseuResponseContext);
	
	void write(final S name, final Map<String, String> tesseuRequestContext, final S inputSource, final Throwable t);

	default List<String> readExpectedExpressions(final S name, final Map<String, String> tesseuRequestContext){
		return Collections.emptyList();
	}
}
