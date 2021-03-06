package br.com.cafebinario.teseu.infrastruct.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.Audit;
import br.com.cafebinario.teseu.infrastruct.database.jpa.entities.HttpRequest;
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
@EqualsAndHashCode(callSuper = false, of = { "id" })
@ToString(callSuper = true)
public class HttpParams extends Audit {
 
	private static final long serialVersionUID = 4004938552296947866L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column
	private String name;

	@Column
	private String value;

	@ManyToOne
	private HttpRequest request;
}
