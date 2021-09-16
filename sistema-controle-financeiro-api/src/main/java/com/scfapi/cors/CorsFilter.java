package com.scfapi.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.scfapi.config.property.ScfApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)//Coloca como prioridade alta pois tem que ser analisado a requisição antes de todas as outras
public class CorsFilter implements Filter{
	
	@Autowired
	private ScfApiProperty scfApiProperty;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		//Casting;
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		//São enviados em todas as requisições, por isso estão fora da condicional;
		response.setHeader("Access-Control-Allow-Origin", scfApiProperty.getOrigemPermitida());
		//Autorização de envio do Cookie de Refresh Token;
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		if ("OPTIONS".equals(request.getMethod()) &&  scfApiProperty.getOrigemPermitida().equals(request.getHeader("Origin"))) {
			
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			response.setHeader("Access-Control-Max-Age", "3600");
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
}