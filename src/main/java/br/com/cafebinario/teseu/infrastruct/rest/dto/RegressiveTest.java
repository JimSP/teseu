package br.com.cafebinario.teseu.infrastruct.rest.dto;

import java.util.LinkedHashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false, of = { "id" })
@ToString(callSuper = true)
public class RegressiveTest {

	private Long id;
	private String name;
	private LinkedHashSet<Api> sequenceOfApis;
	
}
