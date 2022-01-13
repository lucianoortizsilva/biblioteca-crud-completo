package com.lucianoortizsilva.crud.pessoa.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lucianoortizsilva.crud.pessoa.dto.PessoaDTO;
import com.lucianoortizsilva.crud.pessoa.model.Pessoa;
import com.lucianoortizsilva.crud.pessoa.service.PessoaService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pessoas")
public class PessoaController {

	private PessoaService pessoaService;

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid final PessoaDTO dto) {
		Pessoa pessoa = this.pessoaService.convertToEntity(dto);
		pessoa = this.pessoaService.insert(pessoa);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pessoa.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody @Valid final PessoaDTO dto, @PathVariable(value = "id", required = true) final Long id) {
		dto.setId(id);
		this.pessoaService.update(dto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id", required = true) final Long id) {
		this.pessoaService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Pessoa> findById(@PathVariable(value = "id", required = true) final Long id) {
		return ResponseEntity.ok().body(this.pessoaService.findById(id));
	}

}