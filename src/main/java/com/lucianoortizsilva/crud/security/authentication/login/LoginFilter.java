package com.lucianoortizsilva.crud.security.authentication.login;

import java.io.IOException;

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

import com.lucianoortizsilva.crud.security.authentication.error.GeraErroBadRequest;
import com.lucianoortizsilva.crud.security.authentication.error.GeraErroNaoAutorizado;
import com.lucianoortizsilva.crud.security.authentication.user.User;
import com.lucianoortizsilva.crud.security.authentication.user.UserDTO;
import com.lucianoortizsilva.crud.security.authentication.user.UserService;
import com.lucianoortizsilva.crud.security.token.TokenJwt;
import com.lucianoortizsilva.crud.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private TokenJwt tokenJwt;

	private UserService userService;

	public LoginFilter(final AuthenticationManager authenticationManager, final UserService userService, final TokenJwt tokenJwt) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.tokenJwt = tokenJwt;
	}
	
	
	
	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) throws AuthenticationException {
		try {
			final UserDTO user = (UserDTO) JsonUtil.convertToObject(req.getInputStream(), UserDTO.class);
			log.info("Solicitando token para usuario com username: {}", user.getUsername());
			final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = userService.getUsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
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
		final String username = ((User) authentication.getPrincipal()).getUsername();
		final String token = this.tokenJwt.generateToken(username);
		response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
		log.info("Authorization: Bearer {}", token);
	}

	
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
			final GeraErroNaoAutorizado erroNaoAutorizado = new GeraErroNaoAutorizado(response);
			erroNaoAutorizado.comMensagem("Usuário e/ou senha inválidos");
		}
	}

}