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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lucianoortizsilva.crud.seguranca.autenticacao.UserService;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroInesperado;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroNaoAutorizado;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroRequisicaoInvalida;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroSemPermissao;
import com.lucianoortizsilva.crud.seguranca.token.TokenJwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @see https://docs.spring.io/spring-security/site/docs/5.4.7/reference/html5/
 *
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (final Exception e) {
			if (e.getCause() instanceof AccessDeniedException) {
				autenticar(request, response, filterChain);
			} else if (e.getCause() instanceof SignatureException) {
				autenticar(request, response, filterChain);
			} else if (e.getCause() instanceof ExpiredJwtException) {
				final GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
				geraErroNaoAutorizado.comMensagem("Token Expirado");
			} else {
				log.error(e.getMessage(), e);
				final GeraErroInesperado geraErroInesperado = new GeraErroInesperado(response);
				geraErroInesperado.comMensagemPadrao();
			}
		} 
	}

	private void autenticar(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) {
		try {
			final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (isEmpty(authorization) || !authorization.startsWith("Bearer")) {
				GeraErroRequisicaoInvalida geraErroRequisicaoInvalida = new GeraErroRequisicaoInvalida(response);
				geraErroRequisicaoInvalida.comMensagem("Authorization inválida");
			} else {
				final UsernamePasswordAuthenticationToken usuarioAutenticado = this.userService.getUsernamePasswordAuthenticationToken(authorization);
				SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
				chain.doFilter(request, response);
			}
		} catch (final UsernameNotFoundException e) {
			log.error(e.getMessage(), e);
			GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
			geraErroNaoAutorizado.comMensagem("Authorization com usuário não encontrado");
		} catch (final TokenJwtException e) {
			log.error(e.getMessage(), e);
			GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
			geraErroNaoAutorizado.comMensagem(e.getMessage());
		} catch (final Exception e) {
			if (e.getCause() instanceof AccessDeniedException) {
				log.error(e.getCause().getMessage(), e.getCause());
				GeraErroSemPermissao geraErroSemPermissao = new GeraErroSemPermissao(response);
				geraErroSemPermissao.comMensagem("Usuário sem permissão");
			} else {
				log.error(e.getMessage(), e);
				GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
				geraErroNaoAutorizado.comMensagem("Authorization inválida");
			}
		} 
	}

}