package com.lucianoortizsilva.crud.configuracao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;
import com.lucianoortizsilva.crud.seguranca.autenticacao.User;
import com.lucianoortizsilva.crud.seguranca.autenticacao.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile(value = "test")
public class ProfileTestConfig {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	private static final String SENHA = "12345";

	@Bean
	public void inicializar() {
		log.info("################ Profile=test inicializado ################");
		
		log.info("Populando a base com dados de [CLIENTE]");
		Stream<Cliente> clientes = Stream.of(
				new Cliente(null, "Luciano Ortiz", "84365299877", LocalDate.of(1984, 01, 31)),
				new Cliente(null, "Liziane Ortiz", "65464465454", LocalDate.of(1989, 06, 8)),
				new Cliente(null, "Mariana Ortiz", "12548664545", LocalDate.of(2011, 11, 28)),
				new Cliente(null, "Rafaela Ortiz", "46544557554", LocalDate.of(2011, 07, 28)),
				new Cliente(null, "Mikaela Ortiz", "78895455624", LocalDate.of(1990, 05, 19)));
		final List<Cliente> pessoasCadastradas = this.clienteRepository.saveAll(clientes.collect(Collectors.toList()));
		pessoasCadastradas.forEach(c -> log.info("ClienteController: {}", c));
		
		log.info("Populando a base com dados de [USER]");
		Stream<User> usuarios = Stream.of(new User(null, "lucianoortizsilva@gmail.com", bCryptPasswordEncoder.encode(SENHA), "teste"),
				                          new User(null, "teste@gmail.com", bCryptPasswordEncoder.encode(SENHA), "teste II"));
		final List<User> usuariosCadastradas = this.userRepository.saveAll(usuarios.collect(Collectors.toList()));
		usuariosCadastradas.forEach(u -> log.info("Usuário: {}", u));
	}

}