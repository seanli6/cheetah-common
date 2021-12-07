package com.telus.ssnscheetah.springboot.kongconsumer.oauth2;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


public class KongRestTemplateInterceptor implements ClientHttpRequestInterceptor {

	private static Log log = LogFactory.getLog(KongRestTemplateInterceptor.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private OAuth2AuthorizationGrantRequest oAuth2AuthGrantRequest;
    private OAuth2AuthorizationManager oAuth2Manager;
    
    
    public  KongRestTemplateInterceptor(OAuth2AuthorizationGrantRequest anAuthRequest) {
		this.oAuth2AuthGrantRequest = anAuthRequest;
		this.oAuth2Manager = OAuth2AuthorizationManager.getInstance();
	}
    
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
					
			String token = oAuth2Manager.getAuthorizationToken(oAuth2AuthGrantRequest);

			log.debug("KongRestTemplateInterceptor token " + token);
		    request.getHeaders().add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token);
		    request.getHeaders().add("env", oAuth2AuthGrantRequest.getEnv());
		    return execution.execute(request, body);	

	}

}
