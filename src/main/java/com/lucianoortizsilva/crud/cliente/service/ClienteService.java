package com.lucianoortizsilva.crud.cliente.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.entity.Cliente;
import com.lucianoortizsilva.crud.cliente.exception.ClienteComCpfDuplicadoException;
import com.lucianoortizsilva.crud.cliente.exception.ClienteNaoEncontradoException;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClienteService {

	private ModelMapper modelMapper;
	private ClienteRepository clienteRepository;

	@Transactional
	public Cliente insert(final Cliente entity) {
		final Optional<Cliente> cliente = this.clienteRepository.findByCpf(entity.getCpf());
		if (cliente.isPresent()) {
			throw new ClienteComCpfDuplicadoException();
		} else {
			return this.clienteRepository.save(entity);
		}
	}

	
	
	@Transactional
	public void delete(final Long id) {
		final Optional<Cliente> cliente = this.clienteRepository.findById(id);
		if (cliente.isPresent()) {
			this.clienteRepository.deleteById(id);
		} else {
			throw new ClienteNaoEncontradoException();
		}
	}

	
	
	public Cliente findById(final Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isPresent()) {
			return cliente.get();
		} else {
			throw new ClienteNaoEncontradoException();
		}
	}

	
	
	@Transactional
	public void update(final ClienteDTO dto) {
		final Optional<Cliente> cliente = this.clienteRepository.findById(dto.getId());
		if (cliente.isPresent()) {
			this.validarCpfDuplicadoAoAtualizarDadosCliente(dto);
			this.clienteRepository.save(this.convertToEntity(dto));
		} else {
			throw new ClienteNaoEncontradoException();
		}
	}

	
	
	private void validarCpfDuplicadoAoAtualizarDadosCliente(final ClienteDTO dto) {
		final Optional<Cliente> clienteCadastrado = this.clienteRepository.findByCpf(dto.getCpf());
		if (clienteCadastrado.isPresent()) {
			final Long idClienteCadastrado = clienteCadastrado.get().getId();
			final Long idClienteParaAtualizar = dto.getId();
			if (!idClienteCadastrado.equals(idClienteParaAtualizar)) {
				throw new ClienteComCpfDuplicadoException();
			}
		}
	}

	
	
	public Cliente convertToEntity(final ClienteDTO dto) {
		return modelMapper.map(dto, Cliente.class);
	}

}