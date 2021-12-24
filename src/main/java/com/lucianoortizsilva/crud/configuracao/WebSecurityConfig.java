package com.lucianoortizsilva.crud.configuracao;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.lucianoortizsilva.crud.seguranca.autenticacao.AutenticacaoFilter;
import com.lucianoortizsilva.crud.seguranca.autenticacao.TokenJWT;
import com.lucianoortizsilva.crud.seguranca.autorizacao.JWTAuthorizationFilter;

/**
 * 
 * https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 * 
 * @author ortiz
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // permite adicionar @PreAuthorize nos endpoints
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Value("${spring.profiles.active}")
	private String profileActive;
	
	private static final String ENDPOINT_DATABASE_H2 = "/h2-console/**";
	private static final String ENDPOINT_CLIENTES = "/clientes";
	private static final String ENDPOINT_LOGIN = "/login";
	
	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenJWT tokenJWT;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Para funcionar a page http://localhost:8080/h2-console:
		if (Arrays.asList(this.env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.addFilter(new AutenticacaoFilter(authenticationManager(), this.tokenJWT));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), this.tokenJWT, this.userDetailsService));
		http.cors().and().csrf().disable();
		
		http.authorizeRequests().antMatchers(ENDPOINT_DATABASE_H2).permitAll()
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, ENDPOINT_CLIENTES).permitAll()
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, ENDPOINT_LOGIN).permitAll()
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, ENDPOINT_CLIENTES).authenticated()
			.and()
			.authorizeRequests().antMatchers(HttpMethod.PUT, ENDPOINT_CLIENTES).authenticated()
			.and()
			.authorizeRequests().antMatchers(HttpMethod.DELETE, ENDPOINT_CLIENTES).authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		if("test".equalsIgnoreCase(profileActive)) {
	        auth.inMemoryAuthentication()
	         .withUser("luciano")
	         .password("12345")
	         .roles("ADMINISTRADOR");
		} else {
			auth.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder());
		}
	}

}