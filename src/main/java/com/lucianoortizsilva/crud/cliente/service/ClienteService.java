package com.lucianoortizsilva.crud.cliente.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.model.Perfil;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;
import com.lucianoortizsilva.crud.exception.DadoDuplicadoException;
import com.lucianoortizsilva.crud.exception.NaoAutorizadoException;
import com.lucianoortizsilva.crud.exception.NaoEncontradoException;
import com.lucianoortizsilva.crud.seguranca.UserService;
import com.lucianoortizsilva.crud.seguranca.autenticacao.UserSpringSecurity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClienteService {

	private ClienteRepository clienteRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public Cliente insert(final Cliente entity) {
		final Optional<Cliente> cliente = this.clienteRepository.findByEmail(entity.getEmail());
		if (cliente.isPresent()) {
			throw new DadoDuplicadoException("Cliente com e-mail: " + entity.getEmail() + " já foi cadastrado!");
		} else {
			return this.clienteRepository.save(entity);
		}
	}

	@Transactional
	public void update(final ClienteDTO dto) {
		final Cliente cliente = getById(dto.getId());
		if (!exists(cliente))
			throw new NaoEncontradoException("Cliente Não Encontrado");
		if (!clienteLogadoIgualClienteInformadoParaEditar(dto))
			throw new NaoAutorizadoException("Não é possível editar dados de outro cliente!");
		this.clienteFromTo(cliente, dto);
		this.clienteRepository.save(cliente);
	}

	@Transactional
	public void delete(final Long id) {
		if (!perfilLogadoIgualAdministrador())
			throw new NaoAutorizadoException("Não tem autorização para deletar clientes.");

		final Cliente cliente = this.getById(id);
		if (exists(cliente)) {
			throw new NaoEncontradoException("Cliente não encontrado");
		} else {
			this.clienteRepository.deleteById(id);
		}
	}
	
	public Cliente fromDTO(final ClienteDTO dto) {
		final Cliente cliente = new Cliente();
		cliente.setId(dto.getId());
		cliente.setCpf(dto.getCpf());
		cliente.setNome(dto.getNome());
		cliente.setEmail(dto.getEmail());
		cliente.setPerfis(dto.getPerfis());
		cliente.setNascimento(dto.getNascimento());
		cliente.setSenha(this.bCryptPasswordEncoder.encode(dto.getSenha()));
		return cliente;
	}
	
	public Optional<Cliente> findById(final Long id) {
		if (clienteIdPesquisadoIgualAoClienteLogado(id) || perfilLogadoIgualAdministrador()) {
			return this.clienteRepository.findById(id);
		} else {
			throw new NaoAutorizadoException("Não tem autorização para visualizar dados de outros clientes.");
		}
	}

	private void clienteFromTo(final Cliente cliente, final ClienteDTO dto) {
		cliente.setCpf(dto.getCpf());
		cliente.setNome(dto.getNome());
		cliente.setEmail(dto.getEmail());
		cliente.setPerfis(dto.getPerfis());
		cliente.setNascimento(dto.getNascimento());
		cliente.setSenha(this.bCryptPasswordEncoder.encode(dto.getSenha()));
	}

	private Cliente getById(final Long id) {
		final Optional<Cliente> optional = this.clienteRepository.findById(id);
		Cliente cliente = null;
		if (optional.isPresent()) {
			cliente = optional.get();
		}
		return cliente;
	}

	private static boolean clienteLogadoIgualClienteInformadoParaEditar(final ClienteDTO cliente) {
		final UserSpringSecurity userSpringSecurity = UserService.authenticated();
		return userSpringSecurity.getId().equals(cliente.getId());
	}

	private static boolean clienteIdPesquisadoIgualAoClienteLogado(final Long id) {
		final UserSpringSecurity userSpringSecurity = UserService.authenticated();
		return userSpringSecurity.getId().equals(id);
	}

	private static boolean perfilLogadoIgualAdministrador() {
		final UserSpringSecurity userSpringSecurity = UserService.authenticated();
		return userSpringSecurity.hasRole(Perfil.ADMINISTRADOR);
	}

	private static boolean exists(final Cliente cliente) {
		return !Objects.isNull(cliente);
	}

}