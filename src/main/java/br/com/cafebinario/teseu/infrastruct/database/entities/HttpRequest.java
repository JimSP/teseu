package br.com.cafebinario.teseu.infrastruct.database.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@NamedEntityGraph(name = "graph.httpRequest.headersAndParamsAndExpectedValues", 
attributeNodes = {
    @NamedAttributeNode(value = "headers"),
    @NamedAttributeNode(value = "params"),
    @NamedAttributeNode(value = "expectedValues")
})
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy="request", orphanRemoval = true)
	private Set<HttpHeaders> headers;
   
	@OneToMany(cascade = CascadeType.ALL, mappedBy="request", orphanRemoval = true)
	private Set<HttpParams> params;

	@OneToMany(cascade = CascadeType.ALL, mappedBy="request", orphanRemoval = true)
	private Set<ExpectedValues> expectedValues;

	@ManyToOne
	private TeseuContext teseuContext;

	@ManyToOne
	private Client client;
}
