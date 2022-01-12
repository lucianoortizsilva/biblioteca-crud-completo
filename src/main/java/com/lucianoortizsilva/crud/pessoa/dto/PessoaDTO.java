package com.lucianoortizsilva.crud.pessoa.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaDTO {

	@JsonIgnore
	private Long id;

	@NotNull
	@NotEmpty
	@Size(max = 120)
	private String nome;

	@NotEmpty
	@Size(min = 11, max = 11)
	private String cpf;

	@NotNull
	private LocalDate nascimento;

	@Email
	@NotEmpty
	private String email;

	@NotEmpty
	private String senha;

	@NotNull
	private Set<Integer> perfis;

}