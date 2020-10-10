package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;
import com.lucianoortizsilva.crud.seguranca.CredencialDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(final AuthenticationManager authenticationManager, final JWTUtil jwtUtil) {
		log.info("Inicializando JWT filter");
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		try {
			final CredencialDTO credencial = new ObjectMapper().readValue(req.getInputStream(), CredencialDTO.class);
			log.info("Solicitando token para usuario com e-mail: {}", credencial.getEmail());
			final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credencial.getEmail(), credencial.getSenha(), new ArrayList<>());
			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		final String username = ((UserSpringSecurity) auth.getPrincipal()).getUsername();
		final String token = jwtUtil.generateToken(username);
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