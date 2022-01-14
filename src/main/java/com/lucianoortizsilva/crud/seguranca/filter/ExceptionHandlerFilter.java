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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lucianoortizsilva.crud.seguranca.error.GeraErroInesperado;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroNaoAutorizado;
import com.lucianoortizsilva.crud.seguranca.error.GeraErroRequisicaoInvalida;
import com.lucianoortizsilva.crud.seguranca.token.Payload;
import com.lucianoortizsilva.crud.seguranca.token.TokenJwt;
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
	private TokenJwt tokenJwt;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (final Exception e) {
			if (e.getCause() instanceof AccessDeniedException) {
				this.autenticar(request, response, filterChain);
			} else if (e.getCause() instanceof SignatureException) {
				this.autenticar(request, response, filterChain);
			} else if(e.getCause() instanceof ExpiredJwtException) {
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
		final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (isEmpty(authorization) || !authorization.startsWith("Bearer")) {
			negarAcessoRequisicaoInvalida(response);
		} else {
			try {
				final UsernamePasswordAuthenticationToken usuarioAutenticado = getUsernamePasswordAuthenticationToken(authorization);
				SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
				chain.doFilter(request, response);
			} catch (final UsernameNotFoundException e) {
				log.error(e.getMessage(), e);
				GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
				geraErroNaoAutorizado.comMensagem("Authorization com usuário não encontrado");
			} catch (final TokenJwtException e) {
				log.error(e.getMessage(), e);
				GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
				geraErroNaoAutorizado.comMensagem(e.getMessage());
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
				GeraErroNaoAutorizado geraErroNaoAutorizado = new GeraErroNaoAutorizado(response);
				geraErroNaoAutorizado.comMensagem("Authorization inválida");
			}
		}
	}

	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(final String authorization) {
		final Payload payload = this.tokenJwt.getPayload(authorization);
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(payload.getLogin());
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	private static void negarAcessoRequisicaoInvalida(final HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		GeraErroRequisicaoInvalida geraErroRequisicaoInvalida = new GeraErroRequisicaoInvalida(response);
		geraErroRequisicaoInvalida.comMensagem("Authorization inválida");
	}

}