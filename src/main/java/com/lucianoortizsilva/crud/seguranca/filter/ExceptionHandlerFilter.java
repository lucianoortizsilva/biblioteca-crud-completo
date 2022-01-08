package com.lucianoortizsilva.crud.seguranca.filter;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lucianoortizsilva.crud.seguranca.error.GeraErroInesperado;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroNaoAutorizado;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroRequisicaoInvalida;
import com.lucianoortizsilva.crud.seguranca.token.Payload;
import com.lucianoortizsilva.crud.seguranca.token.TokenJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Autowired
	private TokenJWT tokenJWT;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (final Exception e) {
			if (e.getCause() instanceof AccessDeniedException) {
				this.autenticar(request, response, filterChain);
			} else {
				final GeraErroInesperado geraErroInesperado = new GeraErroInesperado(response);
				geraErroInesperado.comMensagem("OPS! Erro inesperado");
			}
		}
	}

	private void autenticar(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) {
		final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (isEmpty(authorization) || !authorization.startsWith("Bearer")) {
			negarAcessoRequisicaoInvalida(response);
		} else {
			try {
				final String token = authorization.substring("Bearer".length()).trim();
				final UsernamePasswordAuthenticationToken usuarioAutenticado = getUsernamePasswordAuthenticationToken(token);
				SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
				chain.doFilter(request, response);
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
				negarAcessoParaUsuarioNaoAutenticado(response);
			}
		}
	}

	
	
	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String token) {
		final Payload payload = this.tokenJWT.getPayload(token);
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(payload.getLogin());
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	
	
	
	private static void negarAcessoRequisicaoInvalida(final HttpServletResponse response) {
		SecurityContextHolder.getContext().setAuthentication(null);
		GeraErroRequisicaoInvalida geraErroRequisicaoInvalida = new GeraErroRequisicaoInvalida(response);
		geraErroRequisicaoInvalida.comMensagem("Authorization invalida");
	}
	
	
	
	private static void negarAcessoParaUsuarioNaoAutenticado(final HttpServletResponse response) {
		SecurityContextHolder.getContext().setAuthentication(null);
		GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
		geraErroNaoAutorizado.comMensagem("Usuario nao autenticado");
	}

}