package br.com.cafebinario.teseu.model;

import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Data;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Data
@Builder
public class TeseuContext {
	
	public Map<String, String> context;
}
