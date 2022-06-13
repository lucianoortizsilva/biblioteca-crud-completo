package com.biblioteca.livro.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.biblioteca.livro.dto.LivroDTO;
import com.biblioteca.livro.entity.Livro;
import com.biblioteca.livro.service.LivroService;

import lombok.AllArgsConstructor;

//@formatter:off
@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/livros")
public class LivroController {

	private LivroService livroService;

	@PreAuthorize("permitAll")
	@GetMapping(value = "/pageable")
	public ResponseEntity<Page<Livro>> getAllLivros(
			@RequestParam(required = false, name = "descricao") String descricao,
			@RequestParam(required = false, defaultValue = "0") int page, 
			@RequestParam(required = false, defaultValue = "5") int size) {
		return new ResponseEntity<>(livroService.findAll(descricao, page, size), HttpStatus.OK);
	}

	
	
	@PostMapping
	@PreAuthorize("permitAll")
	public ResponseEntity<Void> insert(@RequestBody @Valid final LivroDTO dto) {
		Livro livro = this.livroService.convertToEntity(dto);
		livro = this.livroService.insert(livro);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livro.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CLIENTE')")
	public ResponseEntity<Void> update(@RequestBody @Valid @P("dto") final LivroDTO dto, @PathVariable(value = "id", required = true) final Long id) {
		dto.setId(id);
		this.livroService.update(dto);
		return ResponseEntity.noContent().build();
	}

	
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable(value = "id", required = true) final Long id) {
		this.livroService.delete(id);
		return ResponseEntity.noContent().build();
	}

	
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CLIENTE') or hasAuthority('ROLE_SUPORTE')")
	public ResponseEntity<Livro> findById(@PathVariable(value = "id", required = true) final Long id) {
		return ResponseEntity.ok().body(this.livroService.findById(id));
	}

}