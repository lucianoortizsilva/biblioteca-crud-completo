package com.lucianoortizsilva.crud.config;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.lucianoortizsilva.crud.pessoa.model.Cliente;
import com.lucianoortizsilva.crud.pessoa.repository.ClienteRepository;

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
		final Cliente clientes[] = {
				new Cliente("Luciano Ortiz", "84365299877", LocalDate.of(1984, 01, 31), "luciano@gmail.com"),
				new Cliente("Liziane Ortiz", "65464465454", LocalDate.of(1989, 06, 8), "liziane@gmail.com"),
				new Cliente("Mariana Ortiz", "12548664545", LocalDate.of(2011, 11, 28), "mariana@gmail.com"),
				new Cliente("Rafaela Ortiz", "46544557554", LocalDate.of(2011, 07, 28), "rafaela@gmail.com"),
				new Cliente("Mikaela Ortiz", "78895455624", LocalDate.of(1990, 05, 19), "mikaela@gmail.com"), };

		log.info("Populando a base com dados de [CLIENTE]");
		final List<Cliente> clientesCadastrados = this.clienteRepository.saveAll(Arrays.asList(clientes));
		clientesCadastrados.forEach(c -> log.info("Cliente: {}", c));

	}

}