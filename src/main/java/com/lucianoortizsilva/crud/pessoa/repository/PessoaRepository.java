package com.lucianoortizsilva.crud.pessoa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucianoortizsilva.crud.pessoa.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	Optional<Pessoa> findByEmail(String email);
	
}