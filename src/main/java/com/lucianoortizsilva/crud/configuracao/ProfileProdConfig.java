package com.lucianoortizsilva.crud.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile(value = "dev")
public class ProfileProdConfig {

	@Bean
	public void inicializar() {
		log.info("################ Profile=prod inicializado ################");
	}

}