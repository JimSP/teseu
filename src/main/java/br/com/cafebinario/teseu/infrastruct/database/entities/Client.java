package br.com.cafebinario.teseu.infrastruct.database.entities;

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
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
public class Client extends Audit {

	private static final long serialVersionUID = 5666849792747756624L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private ClientState clientState;
	
	@OneToMany
	private List<TeseuContext> teseuContexts;
}
