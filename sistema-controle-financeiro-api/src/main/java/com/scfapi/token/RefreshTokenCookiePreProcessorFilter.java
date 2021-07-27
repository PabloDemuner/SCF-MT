package com.scfapi.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
 * Classe que filtra e encapsula o Cookie Http para o JavaScript não ter acesso,
 *  tornando a aplicação mais segura.
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)//Coloca como prioridade alta pois tem que ser analisado a requisição antes de todas as outras
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest Request = (HttpServletRequest) servletRequest;

		if ("/oauth/token".equalsIgnoreCase(Request.getRequestURI())
				&& "refresh_token".equals(Request.getParameter("grant_type"))
				&& Request.getCookies() != null) {
			
			for (Cookie cookie : Request.getCookies()) {

				if (cookie.getName().equals("refreshToken")) {
					String refreshToken = cookie.getValue();
					Request = new MyServletRequestWrapper(Request, refreshToken);
				}
			}
		}
			
//			 String refreshToken = 
//				        Stream.of(Request.getCookies())
//				            .filter(cookie -> "refreshToken".equals(cookie.getName()))
//				            .findFirst()
//				            .map(cookie -> cookie.getValue())
//				            .orElse(null);
//
//			 Request = new MyServletRequestWrapper(Request, refreshToken);
			
		filterChain.doFilter(Request, servletResponse);
	}

	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}

		@Override
		public Map<String, String[]> getParameterMap() {

			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true);
			return map;
		}

	}
}
