package br.com.cafebinario.teseu.infrastruct.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cafebinario.teseu.api.ContextVariableApi;
import br.com.cafebinario.teseu.infrastruct.adapter.ContextVariableAdapterInterface;
import br.com.cafebinario.teseu.infrastruct.rest.dto.ContextVariable; 

@RestController
@RequestMapping("/context")
@Profile("web") 
public class ContextVariableRestAPI implements ContextVariableApi {

	@Autowired
	private ContextVariableAdapterInterface contextVariableAdapterInterface;
	 
	@Override
	@PostMapping 
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody ContextVariable save(@Valid @RequestBody final ContextVariable contextVariable) {
		contextVariable.setName("Default");
		return contextVariableAdapterInterface.save(contextVariable);
	}
 
	@Override
	@GetMapping 
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody List<ContextVariable> getAll(final Pageable pageable) {
		return contextVariableAdapterInterface.getAll(pageable);
	}

	@Override
	@GetMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody ContextVariable getById(@PathVariable(name = "id", required = true) final Long id) {
		return contextVariableAdapterInterface.getById(id);
	}

	@Override
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void remover(Long id) {
		contextVariableAdapterInterface.delete(id);
		
	}

	@Override
	@PutMapping
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody void update(@Valid @RequestBody final ContextVariable contextVariable) {
		contextVariableAdapterInterface.save(contextVariable);
	}

}
