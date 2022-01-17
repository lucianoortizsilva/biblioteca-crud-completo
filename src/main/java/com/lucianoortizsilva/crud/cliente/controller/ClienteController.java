package com.lucianoortizsilva.crud.cliente.controller;

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

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.service.ClienteService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/clientes")
public class ClienteController {

	private ClienteService clienteService;

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid final ClienteDTO dto) {
		Cliente cliente = this.clienteService.convertToEntity(dto);
		cliente = this.clienteService.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody @Valid final ClienteDTO dto, @PathVariable(value = "id", required = true) final Long id) {
		dto.setId(id);
		this.clienteService.update(dto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id", required = true) final Long id) {
		this.clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> findById(@PathVariable(value = "id", required = true) final Long id) {
		return ResponseEntity.ok().body(this.clienteService.findById(id));
	}

}