package br.com.cafebinario.teseu.infrastruct.database.entities;

import java.util.Set;

import javax.persistence.CascadeType;
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
	 
	@OneToMany(cascade = CascadeType.ALL, mappedBy="httpResponse", orphanRemoval = true)
	private Set<HttpResponseHeaders> httpResponseHeaders;

	private Integer executionStatus;
	
//	public ExecutionStatus getExecutionStatus() {
 ///       return ExecutionStatus.getType(this.executionStatus);
  //  }
	
}
