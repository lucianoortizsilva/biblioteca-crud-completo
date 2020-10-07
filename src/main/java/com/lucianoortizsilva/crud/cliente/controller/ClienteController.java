package com.lucianoortizsilva.crud.cliente.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.service.ClienteService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/clientes")
public class ClienteController {

	private ClienteService clienteService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> findById(@PathVariable(value = "id") final Long id) {
		final Cliente cliente = this.clienteService.findById(id);
		if (Objects.isNull(cliente)) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok().body(cliente);
		}
	}

}