package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucianoortizsilva.crud.seguranca.autenticacao.model.Permission;
import com.lucianoortizsilva.crud.seguranca.autenticacao.model.Role;
import com.lucianoortizsilva.crud.seguranca.autenticacao.model.User;
import com.lucianoortizsilva.crud.seguranca.repository.UserRepository;
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
		final User user = (User) this.loadUserByUsername(payload.getLogin());
		final List<String> permissions = getPermissions(user.getRoles());
		final List<GrantedAuthority> authorities = getGrantedAuthorities(permissions);
		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	}

	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String username, final String password) {
		return new UsernamePasswordAuthenticationToken(username, password);
	}

	private List<String> getPermissions(final List<Role> roles) {
		final List<String> permissionsAll = new ArrayList<>();
		final List<Permission> permissions = new ArrayList<>();
		for (final Role role : roles) {
			permissionsAll.add(role.getName());
			permissions.addAll(role.getPermissions());
		}
		for (final Permission permission : permissions) {
			permissionsAll.add(permission.getName());
		}
		return permissionsAll;
	}

	private List<GrantedAuthority> getGrantedAuthorities(final List<String> permissions) {
		final List<GrantedAuthority> grantedAuthority = new ArrayList<>();
		for (final String permission : permissions) {
			grantedAuthority.add(new SimpleGrantedAuthority(permission));
		}
		return grantedAuthority;
	}

}