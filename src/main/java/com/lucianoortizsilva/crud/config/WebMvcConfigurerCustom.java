package com.lucianoortizsilva.crud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lucianoortizsilva.crud.security.token.TokenJwtInterceptor;

@Configuration
public class WebMvcConfigurerCustom implements WebMvcConfigurer {

	private TokenJwtInterceptor tokenJwtInterceptor;

	public WebMvcConfigurerCustom(TokenJwtInterceptor tokenJwtInterceptor) {
		this.tokenJwtInterceptor = tokenJwtInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenJwtInterceptor);
	}
}
