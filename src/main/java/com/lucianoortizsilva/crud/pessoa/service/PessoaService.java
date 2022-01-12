package com.lucianoortizsilva.crud.pessoa.service;

import java.util.Objects;
import java.util.Optional;

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
public class PessoaService {

	private PessoaRepository pessoaRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public Pessoa insert(final Pessoa entity) {
		final Optional<Pessoa> pessoa = this.pessoaRepository.findByEmail(entity.getEmail());
		if (pessoa.isPresent()) {
			throw new DadoDuplicadoException("Pessoa com e-mail: " + entity.getEmail() + " já foi cadastrada!");
		} else {
			return this.pessoaRepository.save(entity);
		}
	}

	@Transactional
	public void update(final PessoaDTO dto, final UserDetailsCustom usuario) {
		final Pessoa pessoa = getById(dto.getId());
		if (!exists(pessoa))
			throw new NaoEncontradoException("Pessoa Não Encontrado");
		if (!pessoaLogadaIgualPessoaInformadoParaEditar(dto, usuario))
			throw new NaoAutorizadoException("Não é possível editar dados de outra pessoa!");
		this.PessoaFromTo(pessoa, dto);
		this.pessoaRepository.save(pessoa);
	}

	@Transactional
	public void delete(final Long id, final UserDetailsCustom usuario) {
		if (!perfilLogadoIgualAdministrador(usuario))
			throw new NaoAutorizadoException("Não tem autorização para deletar pessoas.");

		final Pessoa pessoa = this.getById(id);
		if (exists(pessoa)) {
			this.pessoaRepository.deleteById(id);
		} else {
			throw new NaoEncontradoException("Pessoa não encontrada");
		}
	}

	public Pessoa fromDTO(final PessoaDTO dto) {
		final Pessoa pessoa = new Pessoa();
		pessoa.setId(dto.getId());
		pessoa.setCpf(dto.getCpf());
		pessoa.setNome(dto.getNome());
		pessoa.setEmail(dto.getEmail());
		pessoa.setPerfis(dto.getPerfis());
		pessoa.setNascimento(dto.getNascimento());
		pessoa.setSenha(this.bCryptPasswordEncoder.encode(dto.getSenha()));
		return pessoa;
	}

	public Optional<Pessoa> findById(final Long id, final UserDetailsCustom usuario) {
		if (pessoaIdPesquisadoIgualPessoaLogada(id, usuario) || perfilLogadoIgualAdministrador(usuario)) {
			return this.pessoaRepository.findById(id);
		} else {
			throw new NaoAutorizadoException("Não tem autorização para visualizar dados de outras pessoas.");
		}
	}

	private void PessoaFromTo(final Pessoa pessoa, final PessoaDTO dto) {
		pessoa.setCpf(dto.getCpf());
		pessoa.setNome(dto.getNome());
		pessoa.setEmail(dto.getEmail());
		pessoa.setPerfis(dto.getPerfis());
		pessoa.setNascimento(dto.getNascimento());
		pessoa.setSenha(this.bCryptPasswordEncoder.encode(dto.getSenha()));
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