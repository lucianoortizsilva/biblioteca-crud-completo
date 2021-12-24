package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;
import com.lucianoortizsilva.crud.seguranca.CredencialDTO;
import com.lucianoortizsilva.crud.seguranca.UserDetailsCustom;
import com.lucianoortizsilva.crud.seguranca.erro.GeraErroInesperado;
import com.lucianoortizsilva.crud.seguranca.erro.GeraErroNaoEncontrado;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutenticacaoFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private TokenJWT tokenJWT;

	public AutenticacaoFilter(final AuthenticationManager authenticationManager, final TokenJWT tokenJWT) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.tokenJWT = tokenJWT;
	}
	
	
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) {
		try {
			final CredencialDTO credencial = new ObjectMapper().readValue(req.getInputStream(), CredencialDTO.class);
			log.info("Solicitando token para usuario com e-mail: {}", credencial.getEmail());
			final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credencial.getEmail(), credencial.getSenha(), new ArrayList<>());
			return authenticationManager.authenticate(authToken);
		} catch (final BadCredentialsException e) {
			final GeraErroNaoEncontrado geraErroNaoEncontrado = new GeraErroNaoEncontrado(res);
			geraErroNaoEncontrado.comMensagem("Usuario nao encontrado");
			log.error(e.getMessage(), e);
		} catch (final Exception e) {
			final GeraErroInesperado geraErroInesperado = new GeraErroInesperado(res);
			geraErroInesperado.comMensagem("Erro inesperado");
			log.error(e.getMessage(), e);
		}
		return authenticationManager.authenticate(null);
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
			final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
			final MensagemErroPadrao mensagemPadraoErro = MensagemErroPadrao
					.builder()
					.status(httpStatus.value())
					.erro(httpStatus.getReasonPhrase())
					.mensagem("Email ou senha invalidos")
					.path("/login")
					.build();
			final Gson gson = new Gson();
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(gson.toJson(mensagemPadraoErro));
			log.error("{}. {}", mensagemPadraoErro.getErro(), mensagemPadraoErro.getMensagem());
		}
	}

}