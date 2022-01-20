package com.lucianoortizsilva.crud.security.token;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenJwtInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenJwt tokenJwt;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
		final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (isNotEmpty(authorization) && authorization.startsWith("Bearer")) {
			final String newToken = this.tokenJwt.updateExpirationDateToken(authorization);
			final String newAuthorization = "Bearer " + newToken;
			response.setHeader(HttpHeaders.AUTHORIZATION, newAuthorization);
			response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
		}
		return true;
	}

}