package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucianoortizsilva.crud.pessoa.model.Pessoa;
import com.lucianoortizsilva.crud.pessoa.repository.PessoaRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		final Optional<Pessoa> pessoa = pessoaRepository.findByEmail(email);
		if (pessoa.isEmpty()) {
			throw new UsernameNotFoundException(email);
		}
		return new UserDetailsCustom(pessoa.get().getId(), pessoa.get().getEmail(), pessoa.get().getSenha(), pessoa.get().getPerfis());
	}

}