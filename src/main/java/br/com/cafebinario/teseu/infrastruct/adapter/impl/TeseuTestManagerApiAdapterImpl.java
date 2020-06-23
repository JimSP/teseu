package br.com.cafebinario.teseu.infrastruct.adapter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cafebinario.teseu.infrastruct.adapter.TeseuTestManagerApiAdapterInterface;
import br.com.cafebinario.teseu.infrastruct.database.entities.TeseuRegressiveTest;
import br.com.cafebinario.teseu.infrastruct.database.repositories.TeseuRegressiveTestRepository;
import br.com.cafebinario.teseu.infrastruct.database.repositories.TeseuRegressiveTestRepositoryCustom;
import lombok.SneakyThrows;
  
@Service
public class TeseuTestManagerApiAdapterImpl implements TeseuTestManagerApiAdapterInterface {
 
	@Autowired
	private TeseuRegressiveTestRepository teseuRegressiveTestRepository;
	
	@Autowired
	private TeseuRegressiveTestRepositoryCustom teseuRegressiveTestRepositoryCustom;
	
	
	@Override
	@Transactional
	public TeseuRegressiveTest save(TeseuRegressiveTest teseuRegressiveTest) {	
		
		teseuRegressiveTest.getTeseuExecutionOrders().forEach(execOrder -> execOrder.setRegressiveTest(teseuRegressiveTest)); 
	  
		return teseuRegressiveTestRepository.save(teseuRegressiveTest);
	}

	@Override
	public Page<TeseuRegressiveTest> getAll(Pageable pageable) {
		return teseuRegressiveTestRepository.findAll(pageable);			  
	}

	@Override
	@SneakyThrows
	public TeseuRegressiveTest getById(Long id) {
		
		return teseuRegressiveTestRepositoryCustom.findOneWithLazyDependencies(id);
		
	//	return teseuRegressiveTestRepository.findById(id)
		//			.orElseThrow(()->new TeseuTestManagerApiNotFoundException(id));
	}
	
	@Override
	public void delete(Long id) {
		teseuRegressiveTestRepository.deleteById(id);
	}

}
