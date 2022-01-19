package com.lucianoortizsilva.crud.cliente.entity;

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
public class Cliente implements Serializable {

	private static final long serialVersionUID = 4497503311326550516L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false, length = 120)
	private String nome;

	@EqualsAndHashCode.Include
	@Column(nullable = false, length = 11, unique = true)
	private String cpf;

	@Column(nullable = false)
	private LocalDate dtNascimento;

	public Cliente() {
		super();
	}

	public Cliente(final Long id, final String cpf) {
		this.id = id;
		this.cpf = cpf;
	}

	public Cliente(final String nome, final String cpf, final LocalDate nascimento) {
		this.nome = nome;
		this.cpf = cpf;
		this.dtNascimento = nascimento;
	}

}