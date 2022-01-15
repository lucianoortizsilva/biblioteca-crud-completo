package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucianoortizsilva.crud.seguranca.token.Payload;
import com.lucianoortizsilva.crud.seguranca.token.TokenJwt;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenJwt tokenJwt;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não existe!"));
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String authorization) {
		final Payload payload = this.tokenJwt.getPayload(authorization);
		final UserDetails userDetails = this.loadUserByUsername(payload.getLogin());
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String username, final String senha, final List<SimpleGrantedAuthority> permissoes) {
		return new UsernamePasswordAuthenticationToken(username, senha, permissoes);
	}

}