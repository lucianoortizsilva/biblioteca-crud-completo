package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroBadRequest;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroNaoAutorizado;
import com.lucianoortizsilva.crud.seguranca.token.TokenJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private TokenJWT tokenJWT;

	
	
	public UsernamePasswordAuthentication(final AuthenticationManager authenticationManager, final TokenJWT tokenJWT) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.tokenJWT = tokenJWT;
	}

	
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) throws AuthenticationException {
		CredencialDTO credencial;
		try {
			credencial = new ObjectMapper().readValue(req.getInputStream(), CredencialDTO.class);
			log.info("Solicitando token para usuario com e-mail: {}", credencial.getEmail());
			final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credencial.getEmail(), credencial.getSenha(), new ArrayList<>());
			return authenticationManager.authenticate(authToken);
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
			final GeraErroBadRequest geraErroBadRequest = new GeraErroBadRequest(res);
			geraErroBadRequest.comMensagem("Credencial invalida");
		}
		return null;
	}

	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		final String username = ((UserDetailsCustom) auth.getPrincipal()).getUsername();
		final String token = tokenJWT.generateToken(username);
		res.addHeader("Authorization", "Bearer " + token);
		res.addHeader("access-control-expose-headers", "Authorization");
		log.info("Token de autenticacao gerado com sucesso para: {}", username);
	}

	
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
			final GeraErroNaoAutorizado erroNaoAutorizado = new GeraErroNaoAutorizado(response);
			erroNaoAutorizado.comMensagem("Usuario e/ou senha invalidos");
		}
	}

}