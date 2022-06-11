package com.biblioteca.livro.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Livro implements Serializable {

	private static final long serialVersionUID = 4497503311326550516L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@EqualsAndHashCode.Include
	@Column(nullable = false, length = 120, unique = true)
	private String isbn;

	@Column(nullable = false, length = 120)
	private String descricao;

	@Column(nullable = false, length = 120)
	private String autor;

	@Column(nullable = false)
	private LocalDate dtLancamento;

	public Livro() {
		super();
	}

	public Livro(final Long id, final String isbn) {
		this.id = id;
		this.isbn = isbn;
	}

	public Livro(final String isbn, final String descricao, final String autor, final LocalDate dtLancamento) {
		this.isbn = isbn;
		this.descricao = descricao;
		this.autor = autor;
		this.dtLancamento = dtLancamento;
	}

}