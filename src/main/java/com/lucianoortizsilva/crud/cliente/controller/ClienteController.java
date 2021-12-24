package com.lucianoortizsilva.crud.cliente.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
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

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.service.ClienteService;
import com.lucianoortizsilva.crud.seguranca.UserDetailsCustom;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/clientes")
public class ClienteController {

	private ClienteService clienteService;

	
	
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody final ClienteDTO dto) {
		Cliente cliente = this.clienteService.fromDTO(dto);
		cliente = this.clienteService.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody final ClienteDTO dto, @PathVariable(value = "id") final Long id, @AuthenticationPrincipal final UserDetailsCustom usuario) {
		dto.setId(id);
		this.clienteService.update(dto, usuario);
		return ResponseEntity.noContent().build();
	}
	
	
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id") final Long id, @AuthenticationPrincipal final UserDetailsCustom usuario) {
		this.clienteService.delete(id, usuario);
		return ResponseEntity.noContent().build();
	}
	
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> findById(@PathVariable(value = "id") final Long id, @AuthenticationPrincipal final UserDetailsCustom usuario) {
		final Optional<Cliente> cliente = this.clienteService.findById(id, usuario);
		if (cliente.isPresent()) {
			return ResponseEntity.ok().body(cliente.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}