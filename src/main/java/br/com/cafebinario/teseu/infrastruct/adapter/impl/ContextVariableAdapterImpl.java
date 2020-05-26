package br.com.cafebinario.teseu.infrastruct.adapter.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cafebinario.teseu.exception.ContextVariableNotFoundException;
import br.com.cafebinario.teseu.infrastruct.adapter.ContextVariableAdapterInterface;
import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.TeseuContext;
import br.com.cafebinario.teseu.infrastruct.database.jpa.repositories.TeseuContextRepository;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ContextVariable;
  
@Service
public class ContextVariableAdapterImpl implements ContextVariableAdapterInterface {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TeseuContextRepository teseuContextRepository;

	@Override
	@Transactional
	public ContextVariable save(ContextVariable contextVariable) {		
		final TeseuContext teseuContext = modelMapper.map(contextVariable, TeseuContext.class); 
		final TeseuContext teseuContextSaved = teseuContextRepository.save(teseuContext);
		return modelMapper.map(teseuContextSaved, ContextVariable.class);
	}

	@Override
	public List<ContextVariable> getAll(Pageable pageable) {
		return teseuContextRepository.findAll(pageable)
					.get()
					.map(teseuContextEntity->modelMapper.map(teseuContextEntity, ContextVariable.class))
					.collect(Collectors.toList());
	}

	@Override
	public ContextVariable getById(Long id) {
		return teseuContextRepository.findById(id)
				.map(teseuContext->modelMapper.map(teseuContext, ContextVariable.class))
				.orElseThrow(()->new ContextVariableNotFoundException(id));
	}

	@Override
	public void delete(Long id) {
		teseuContextRepository.deleteById(id);
		
	}
	
}
