package com.lucianoortizsilva.crud.seguranca.autorizacao;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.lucianoortizsilva.crud.seguranca.autenticacao.TokenJWT;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private TokenJWT tokenJWT;

	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, TokenJWT tokenJWT, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.tokenJWT = tokenJWT;
		this.userDetailsService = userDetailsService;
	}

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		final String authorization = request.getHeader("Authorization");
		
		if(authorization != null && authorization.startsWith("Bearer ")) {
		   final String token = authorization.substring(7); 
		   UsernamePasswordAuthenticationToken authenticationToken = this.getAuthenticantion(token);
		   if(authenticationToken != null) {
			  SecurityContextHolder.getContext().setAuthentication(authenticationToken); 
		   }
		}
		chain.doFilter(request, response);
		
	}


	private UsernamePasswordAuthenticationToken getAuthenticantion(String token) {
		if(this.tokenJWT.tokenValido(token)) {
			final String username = this.tokenJWT.getUsername(token); 	
			final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		}
		return null;
	}
}
