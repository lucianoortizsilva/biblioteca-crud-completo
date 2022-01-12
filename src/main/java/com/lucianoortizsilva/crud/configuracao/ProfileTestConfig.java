package com.lucianoortizsilva.crud.configuracao;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lucianoortizsilva.crud.pessoa.model.Pessoa;
import com.lucianoortizsilva.crud.pessoa.repository.PessoaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile(value = "test")
public class ProfileTestConfig {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private static final String SENHA = "12345";
	
	@Bean
	public void inicializar() {
		log.info("################ Profile=test inicializado ################");
		final Pessoa pessoas[] = {
				new Pessoa("Luciano Ortiz", "84365299877", LocalDate.of(1984, 01, 31), "luciano@gmail.com", this.bCryptPasswordEncoder.encode(SENHA)),
				new Pessoa("Liziane Ortiz", "65464465454", LocalDate.of(1989, 06, 8), "liziane@gmail.com", this.bCryptPasswordEncoder.encode(SENHA)),
				new Pessoa("Mariana Ortiz", "12548664545", LocalDate.of(2011, 11, 28), "mariana@gmail.com", this.bCryptPasswordEncoder.encode(SENHA)),
				new Pessoa("Rafaela Ortiz", "46544557554", LocalDate.of(2011, 07, 28), "rafaela@gmail.com", this.bCryptPasswordEncoder.encode(SENHA)),
				new Pessoa("Mikaela Ortiz", "78895455624", LocalDate.of(1990, 05, 19), "mikaela@gmail.com", this.bCryptPasswordEncoder.encode(SENHA)), };

		log.info("Populando a base com dados de [PESSOA]");
		final List<Pessoa> pessoasCadastradas = this.pessoaRepository.saveAll(Arrays.asList(pessoas));
		pessoasCadastradas.forEach(c -> log.info("Pessoa: {}", c));

	}

}