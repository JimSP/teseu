package br.com.cafebinario.teseu.infrastruct.database.jpa.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.cafebinario.teseu.infrastruct.database.entities.HttpParams;
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
public class HttpRequest extends Audit {

	private static final long serialVersionUID = -4511985470857260915L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private HttpMethod method;

	@Column(nullable = false)
	private String host;

	@Column
	private String path;

	@Column
	private String body;

	@OneToMany
	private List<HttpHeaders> headers;
 
	@OneToMany
	private List<HttpParams> params;
 
	@ManyToOne
	private TeseuContext teseuContext;

	@ManyToOne
	private Client client;
}
