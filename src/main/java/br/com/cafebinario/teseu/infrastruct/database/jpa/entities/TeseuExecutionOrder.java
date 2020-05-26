package br.com.cafebinario.teseu.infrastruct.database.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
public class TeseuExecutionOrder extends Audit {

	private static final long serialVersionUID = 4783686341277834736L;
	
	public static String[] orderBy() {
		
		return new String[] { "executionOrder" };
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(nullable = false)
	private String testName;
	
	@Column(nullable = false)
	private String apiName;
	
	@Column(nullable = false)
	private Integer executionOrder;
}
