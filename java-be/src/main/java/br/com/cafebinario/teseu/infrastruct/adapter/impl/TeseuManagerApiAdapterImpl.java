package br.com.cafebinario.teseu.infrastruct.adapter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cafebinario.teseu.exception.ContextVariableNotFoundException;
import br.com.cafebinario.teseu.infrastruct.adapter.TeseuManagerApiAdapterInterface;
import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.HttpRequest;
import br.com.cafebinario.teseu.infrastruct.database.jpa.repositories.HttpRequestRepository;
  
@Service
public class TeseuManagerApiAdapterImpl implements TeseuManagerApiAdapterInterface {
 
	@Autowired
	private HttpRequestRepository httpRequestRepository;
	
	@Override
	@Transactional
	public HttpRequest save(HttpRequest httpRequest) {		 
		return httpRequestRepository.save(httpRequest);
	}

	@Override
	public Page<HttpRequest> getAll(Pageable pageable) {
		return httpRequestRepository.findAll(pageable);			  
	}

	@Override
	public HttpRequest getById(Long id) {
		return httpRequestRepository.findById(id)
					.orElseThrow(()->new ContextVariableNotFoundException(id));
	}
	
	@Override
	public void delete(Long id) {
		httpRequestRepository.deleteById(id);
	}
	
}
