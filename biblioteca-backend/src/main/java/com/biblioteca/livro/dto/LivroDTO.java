package com.biblioteca.livro.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class LivroDTO {

	@JsonIgnore
	private Long id;

	@NotEmpty
	@Size(max = 120)
	private String isbn;
	
	@NotEmpty
	@Size(max = 120)
	private String descricao;

	@NotEmpty
	@Size(max = 120)
	private String autor;

	@NotNull
	private LocalDate dtLancamento;

}