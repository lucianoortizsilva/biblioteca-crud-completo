package com.lucianoortizsilva.crud.cliente.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClienteDTO {

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
	private LocalDate dtNascimento;

}