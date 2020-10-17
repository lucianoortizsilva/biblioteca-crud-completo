package com.lucianoortizsilva.crud.cliente.util;

import java.time.LocalDate;
import java.util.Optional;

import com.lucianoortizsilva.crud.cliente.model.Cliente;

public class ClienteStub {

	public static Optional<Cliente> getCliente() {
		return Optional.of(new Cliente("Luciano", "91458266547", LocalDate.of(2020, 01, 31), "lucianoortizsilva@gmail.com", "123456"));
	}
	
}