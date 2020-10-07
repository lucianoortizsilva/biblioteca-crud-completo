package com.lucianoortizsilva.crud.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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

	private static final String[] ACESSO_PUBLIC = { 
			"/h2-console/**", 
			"/swagger-resources/**", 
			"/swagger-ui.html",
			"/v2/api-docs", 
			"/webjars/**" 
	};

	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Para funcionar a page http://localhost:8080/h2-console:
		if (Arrays.asList(this.env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		http.cors().and().csrf().disable();
		http.authorizeRequests().antMatchers(ACESSO_PUBLIC).permitAll().anyRequest().authenticated();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

}