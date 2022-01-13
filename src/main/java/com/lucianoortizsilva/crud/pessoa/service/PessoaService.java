package com.lucianoortizsilva.crud.pessoa.service;

import static com.lucianoortizsilva.crud.seguranca.util.AuthenticationUtil.getCurrentUser;

import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucianoortizsilva.crud.exception.DadoDuplicadoException;
import com.lucianoortizsilva.crud.exception.NaoAutorizadoException;
import com.lucianoortizsilva.crud.exception.NaoEncontradoException;
import com.lucianoortizsilva.crud.pessoa.dto.PessoaDTO;
import com.lucianoortizsilva.crud.pessoa.model.Perfil;
import com.lucianoortizsilva.crud.pessoa.model.Pessoa;
import com.lucianoortizsilva.crud.pessoa.repository.PessoaRepository;
import com.lucianoortizsilva.crud.seguranca.autenticacao.UserDetailsCustom;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PessoaService {

	
	private ModelMapper modelMapper;
	private PessoaRepository pessoaRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	
	@Transactional
	@PreAuthorize("permitAll")
	public Pessoa insert(final Pessoa entity) {
		final Optional<Pessoa> pessoa = this.pessoaRepository.findByEmail(entity.getEmail());
		if (pessoa.isPresent()) {
			throw new DadoDuplicadoException("Pessoa com e-mail: " + entity.getEmail() + " já foi cadastrada!");
		} else {
			return this.pessoaRepository.save(entity);
		}
	}
	
	
	
	@Transactional
	@PreAuthorize("#dto.id != null and hasAuthority('ADMINISTRADOR')")
	public void update(@P("dto") final PessoaDTO dto) {
		Pessoa pessoa = getById(dto.getId());

		if (!exists(pessoa))
			throw new NaoEncontradoException("Pessoa Não Encontrada");

		if (!pessoaLogadaIgualPessoaInformadoParaEditar(dto, getCurrentUser()))
			throw new NaoAutorizadoException("Não é possível editar dados de outra pessoa!");

		pessoa = this.convertToEntity(dto);

		this.pessoaRepository.save(pessoa);
	}

	

	@Transactional
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public void delete(final Long id) {
		if (!perfilLogadoIgualAdministrador(getCurrentUser()))
			throw new NaoAutorizadoException("Não tem autorização para deletar pessoas.");

		final Pessoa pessoa = this.getById(id);
		
		if (exists(pessoa)) {
			this.pessoaRepository.deleteById(id);
		} else {
			throw new NaoEncontradoException("Pessoa não encontrada");
		}
	}

	
	
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public Optional<Pessoa> findById(final Long id) {
		if (pessoaIdPesquisadoIgualPessoaLogada(id, getCurrentUser()) || perfilLogadoIgualAdministrador(getCurrentUser())) {
			return this.pessoaRepository.findById(id);
		} else {
			throw new NaoAutorizadoException("Não tem autorização para visualizar dados de outras pessoas.");
		}
	}
	
	
	
	@PreAuthorize("permitAll")
	public Pessoa convertToEntity(final PessoaDTO dto) {
		Pessoa pessoa = modelMapper.map(dto, Pessoa.class);
		pessoa.setSenha(this.bCryptPasswordEncoder.encode(dto.getSenha()));
		return pessoa;
	}

	
	
	private Pessoa getById(final Long id) {
		final Optional<Pessoa> optional = this.pessoaRepository.findById(id);
		Pessoa pessoa = null;
		if (optional.isPresent()) {
			pessoa = optional.get();
		}
		return pessoa;
	}

	private static boolean pessoaLogadaIgualPessoaInformadoParaEditar(final PessoaDTO dto, UserDetailsCustom usuario) {
		return usuario.getId().equals(dto.getId());
	}

	private static boolean pessoaIdPesquisadoIgualPessoaLogada(final Long id, final UserDetailsCustom usuario) {
		return usuario.getId().equals(id);
	}

	private static boolean perfilLogadoIgualAdministrador(UserDetailsCustom usuario) {
		return usuario.hasRole(Perfil.ADMINISTRADOR);
	}

	private static boolean exists(final Pessoa pessoa) {
		return !Objects.isNull(pessoa);
	}

}