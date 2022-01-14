package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lucianoortizsilva.crud.seguranca.error.GeraErroBadRequest;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroNaoAutorizado;
import com.lucianoortizsilva.crud.seguranca.token.TokenJwt;
import com.lucianoortizsilva.crud.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private TokenJwt tokenJwt;

	private UserDetailServiceImpl userDetailServiceImpl;
	
	
	public UsernamePasswordAuthentication(final AuthenticationManager authenticationManager, final UserDetailServiceImpl userDetailServiceImpl, final TokenJwt tokenJwt) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.userDetailServiceImpl = userDetailServiceImpl;
		this.tokenJwt = tokenJwt;
	}

	
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) throws AuthenticationException {
		try {
			final CredencialDTO credencial = (CredencialDTO) JsonUtil.convertToObject(req.getInputStream(), CredencialDTO.class);
			log.info("Solicitando token para usuario com e-mail: {}", credencial.getEmail());
			final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = userDetailServiceImpl.getUsernamePasswordAuthenticationToken(credencial.getEmail(), credencial.getSenha(), new ArrayList<>());
			return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			final GeraErroBadRequest geraErroBadRequest = new GeraErroBadRequest(res);
			geraErroBadRequest.comMensagem(e.getMessage());
		}
		return null;
	}

	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		final String username = ((UserDetailsCustom) authentication.getPrincipal()).getUsername();
		final String token = this.tokenJwt.generateToken(username);
		response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
		log.info("Authorization: Bearer {}", token);
	}

	
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
			final GeraErroNaoAutorizado erroNaoAutorizado = new GeraErroNaoAutorizado(response);
			erroNaoAutorizado.comMensagem("Usuario e/ou senha invalidos");
		}
	}

}