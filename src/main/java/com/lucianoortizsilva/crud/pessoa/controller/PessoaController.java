package com.lucianoortizsilva.crud.pessoa.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.lucianoortizsilva.crud.seguranca.autenticacao.UserDetailsCustom;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pessoas")
public class PessoaController {

	private PessoaService pessoaService;

	
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid final PessoaDTO dto) {
		Pessoa pessoa = this.pessoaService.fromDTO(dto);
		pessoa = this.pessoaService.insert(pessoa);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pessoa.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> update(@RequestBody @Valid final PessoaDTO dto, @PathVariable(value = "id", required = true) final Long id, @AuthenticationPrincipal final UserDetailsCustom usuario) {
		dto.setId(id);
		this.pessoaService.update(dto, usuario);
		return ResponseEntity.noContent().build();
	}
	
	

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> delete(@PathVariable(value = "id", required = true) final Long id, @AuthenticationPrincipal final UserDetailsCustom usuario) {
		this.pessoaService.delete(id, usuario);
		return ResponseEntity.noContent().build();
	}
	
	
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Pessoa> findById(@PathVariable(value = "id", required = true) final Long id, @AuthenticationPrincipal final UserDetailsCustom usuario) {
		final Optional<Pessoa> pessoa = this.pessoaService.findById(id, usuario);
		if (pessoa.isPresent()) {
			return ResponseEntity.ok().body(pessoa.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}