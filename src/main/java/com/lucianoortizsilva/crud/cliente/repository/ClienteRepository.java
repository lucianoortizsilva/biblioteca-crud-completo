package com.lucianoortizsilva.crud.cliente.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.lucianoortizsilva.crud.cliente.entity.Cliente;

@Repository
public interface ClienteRepository extends PagingAndSortingRepository<Cliente, Long> {

	Optional<Cliente> findByCpf(String cpf);

	Page<Cliente> findByNomeContaining(String nome, Pageable pageable);

	Page<Cliente> findAll(Pageable pageable);
	
}