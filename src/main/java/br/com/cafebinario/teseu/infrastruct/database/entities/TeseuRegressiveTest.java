package br.com.cafebinario.teseu.infrastruct.database.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@NamedEntityGraph(name = "graph.teseuRegressiveTest.executionsOrders", 
attributeNodes = {
    @NamedAttributeNode(value = "teseuExecutionOrders") 
})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false, of = { "id" })
@ToString(callSuper = true)
public class TeseuRegressiveTest extends Audit {

	private static final long serialVersionUID = -4613096910716079547L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(nullable = false)   
	private String name;
	 
	@OneToMany(cascade = CascadeType.ALL, mappedBy="regressiveTest", orphanRemoval = true)
	private List<TeseuExecutionOrder> teseuExecutionOrders;
 
}
