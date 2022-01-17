package com.lucianoortizsilva.crud.configuracao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile(value = "test")
public class ProfileTestConfig {

	@Autowired
	private ClienteRepository clienteRepository;

	@Bean
	public void inicializar() {
		log.info("################ Profile=test inicializado ################");
		final Cliente homemAranha = new Cliente("Homem Aranha", "54096739057", LocalDate.of(1984, 01, 31));
		final Cliente superHomem = new Cliente("Super Homem", "54671702010", LocalDate.of(1988, 01, 21));
		final Cliente mulherMaravilha = new Cliente("Mulher Maravilha", "70585164053", LocalDate.of(1981, 10, 07));
		final Cliente batman = new Cliente("Batman", "52678324052", LocalDate.of(1990, 05, 17));
		final List<Cliente> clientes = List.of(homemAranha, superHomem, mulherMaravilha, batman);
		clienteRepository.saveAll(clientes);
		clientes.forEach(cliente -> log.info("\nCliente cadastrado: {}", cliente));
	}

}