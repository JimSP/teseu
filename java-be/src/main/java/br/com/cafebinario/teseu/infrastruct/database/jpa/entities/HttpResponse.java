package br.com.cafebinario.teseu.infrastruct.database.jpa.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class HttpResponse extends Audit{

	private static final long serialVersionUID = 3930587554096629155L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(nullable = false)
	private String inputSource;
	
	@Column(nullable = false)
	private Integer statusCode;
	
	@Column
	private String body;
	
	@OneToMany
	private List<HttpResponseHeaders> httpResponseHeaders;
}
