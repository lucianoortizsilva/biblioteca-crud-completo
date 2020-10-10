package com.lucianoortizsilva.crud.cliente.util;

import java.time.LocalDate;

import com.lucianoortizsilva.crud.cliente.model.Cliente;

public class ClienteStub {

	public static Cliente getCliente() {
		return new Cliente("Luciano", "91458266547", LocalDate.of(2020, 01, 31), "lucianoortizsilva@gmail.com", "123456");
	}
	
}