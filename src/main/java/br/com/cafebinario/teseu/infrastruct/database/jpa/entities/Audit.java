package br.com.cafebinario.teseu.infrastruct.database.jpa.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Audit implements Serializable {

	private static final long serialVersionUID = 6121186005036467394L;

	@CreatedDate
	protected LocalDateTime create;

	@LastModifiedDate
	protected LocalDateTime alter;

}
