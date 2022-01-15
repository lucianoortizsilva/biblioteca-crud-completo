package com.lucianoortizsilva.crud.cliente.service;

import static com.lucianoortizsilva.crud.seguranca.util.AuthenticationUtil.getCurrentUser;

import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucianoortizsilva.crud.cliente.dto.ClienteDTO;
import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.model.Perfil;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;
import com.lucianoortizsilva.crud.exception.DadoDuplicadoException;
import com.lucianoortizsilva.crud.exception.NaoAutorizadoException;
import com.lucianoortizsilva.crud.exception.NaoEncontradoException;
import com.lucianoortizsilva.crud.seguranca.autenticacao.User;

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
	@PreAuthorize("#dto.id != null and hasAuthority('ADMINISTRADOR')")
	public void update(@P("dto") final ClienteDTO dto) {
		Cliente cliente = getById(dto.getId());

		if (!exists(cliente))
			throw new NaoEncontradoException("Usuario Não Encontrada");

		if (!pessoaLogadaIgualPessoaInformadoParaEditar(dto, getCurrentUser()))
			throw new NaoAutorizadoException("Não é possível editar dados de outra pessoa!");

		cliente = this.convertToEntity(dto);

		this.clienteRepository.save(cliente);
	}

	
	
	@Transactional
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public void delete(final Long id) {
		if (!perfilLogadoIgualAdministrador(getCurrentUser()))
			throw new NaoAutorizadoException("Não tem autorização para deletar pessoas.");

		final Cliente cliente = this.getById(id);

		if (exists(cliente)) {
			this.clienteRepository.deleteById(id);
		} else {
			throw new NaoEncontradoException("Usuario não encontrada");
		}
	}

	
	
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public Cliente findById(final Long id) {
		User usuario = getCurrentUser();
		if (pessoaIdPesquisadoIgualPessoaLogada(id, usuario) || perfilLogadoIgualAdministrador(usuario)) {
			Optional<Cliente> cliente = clienteRepository.findById(id);
			if (cliente.isPresent()) {
				return cliente.get();
			} else {
				throw new NaoEncontradoException("Cliente não encontrado");
			}
		} else {
			throw new NaoAutorizadoException("Não tem autorização para visualizar dados de outros clientes.");
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

	private static boolean pessoaLogadaIgualPessoaInformadoParaEditar(final ClienteDTO dto, User usuario) {
		return usuario.getId().equals(dto.getId());
	}

	private static boolean pessoaIdPesquisadoIgualPessoaLogada(final Long id, final User usuario) {
		return usuario.getId().equals(id);
	}

	private static boolean perfilLogadoIgualAdministrador(User usuario) {
		return usuario.hasRole(Perfil.ADMINISTRADOR);
	}

	private static boolean exists(final Cliente cliente) {
		return !Objects.isNull(cliente);
	}

}