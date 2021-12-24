package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucianoortizsilva.crud.cliente.model.Cliente;
import com.lucianoortizsilva.crud.cliente.repository.ClienteRepository;
import com.lucianoortizsilva.crud.seguranca.UserDetailsCustom;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		final Optional<Cliente> cliente = clienteRepository.findByEmail(email);
		if (cliente.isEmpty()) {
			throw new UsernameNotFoundException(email);
		}
		return new UserDetailsCustom(cliente.get().getId(), cliente.get().getEmail(), cliente.get().getSenha(), cliente.get().getPerfis());
	}

}