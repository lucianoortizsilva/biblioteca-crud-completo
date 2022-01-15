package com.lucianoortizsilva.crud.cliente.service;

import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;
import com.lucianoortizsilva.crud.exception.DadoDuplicadoException;
import com.lucianoortizsilva.crud.exception.NaoEncontradoException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ClienteService {

	private ModelMapper modelMapper;
	private ClienteRepository clienteRepository;

	@Transactional
	@PreAuthorize("permitAll")
	public Cliente insert(final Cliente entity) {
		final Optional<Cliente> cliente = this.clienteRepository.findByCpf(entity.getCpf());
		if (cliente.isPresent()) {
			throw new DadoDuplicadoException("Cliente com CPF: " + entity.getCpf() + " já foi cadastrado!");
		} else {
			return this.clienteRepository.save(entity);
		}
	}

	@Transactional
	@PreAuthorize("#dto.id != null and (hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CLIENTE'))")
	public void update(@P("dto") final ClienteDTO dto) {
		Cliente cliente = getById(dto.getId());
		if (exists(cliente)) {
			cliente = this.convertToEntity(dto);
			this.clienteRepository.save(cliente);
		} else {
			throw new NaoEncontradoException("Usuario Não Encontrada");
		}
	}

	@Transactional
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public void delete(final Long id) {
		final Cliente cliente = this.getById(id);
		if (exists(cliente)) {
			this.clienteRepository.deleteById(id);
		} else {
			throw new NaoEncontradoException("Usuario não encontrada");
		}
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CLIENTE') or hasAuthority('ROLE_SUPORTE')")
	public Cliente findById(final Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isPresent()) {
			return cliente.get();
		} else {
			throw new NaoEncontradoException("Cliente não encontrado");
		}
	}

	@PreAuthorize("permitAll")
	public Cliente convertToEntity(final ClienteDTO dto) {
		return modelMapper.map(dto, Cliente.class);
	}

	private Cliente getById(final Long id) {
		final Optional<Cliente> optional = this.clienteRepository.findById(id);
		Cliente cliente = null;
		if (optional.isPresent()) {
			cliente = optional.get();
		}
		return cliente;
	}

	private static boolean exists(final Cliente cliente) {
		return !Objects.isNull(cliente);
	}

}