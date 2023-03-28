package com.scfapi.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.scfapi.config.property.ScfApiProperty;

/*Classe de config de Refresh Token para 
 * transformar em um Cookie HTTP
 */
@Profile("oauth-security")
@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{
	
	@Autowired
	private ScfApiProperty scfApiProperty;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		//Conversão(Casting) para ServletServerHttpRequest e ServletServerHttpResponse
		HttpServletRequest httpServletRequest = ((ServletServerHttpRequest)request).getServletRequest();
		HttpServletResponse httpServletResponse = ((ServletServerHttpResponse)response).getServletResponse();
		
		//Conversão do corpo do Token para o setResfreshToken de DefaultOAuth2AccessToken
		DefaultOAuth2AccessToken defaultToken = (DefaultOAuth2AccessToken) body;
		
		String refreshToken = body.getRefreshToken().getValue();
		adicionaRefreshTokenNoCookie(refreshToken, httpServletRequest, httpServletResponse);
		removeRefreshTokenNoCookie(defaultToken);
		return body;
	}

	private void removeRefreshTokenNoCookie(DefaultOAuth2AccessToken defaultToken) {
		defaultToken.setRefreshToken(null);
	}

	private void adicionaRefreshTokenNoCookie(String refreshToken, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(scfApiProperty.getSeguranca().isEnableHttps());
		refreshTokenCookie.setPath(httpServletRequest.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(259200);
		httpServletResponse.addCookie(refreshTokenCookie);
	}

	
}
