package com.lucianoortizsilva.crud.pessoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucianoortizsilva.crud.pessoa.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}