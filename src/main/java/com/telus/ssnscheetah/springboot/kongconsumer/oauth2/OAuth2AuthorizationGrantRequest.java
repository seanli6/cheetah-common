package com.telus.ssnscheetah.springboot.kongconsumer.oauth2;

import java.util.Collections;
import java.util.Set;

public class OAuth2AuthorizationGrantRequest {

	private String clientId;
	private String clientSecret;
	private String authorizationGrantType;
	private Set<String> scopes = Collections.emptySet();
	private String authorizationUrl;
	private String env;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getAuthorizationGrantType() {
		return authorizationGrantType;
	}
	public void setAuthorizationGrantType(String authorizationGrantType) {
		this.authorizationGrantType = authorizationGrantType;
	}
	public Set<String> getScopes() {
		return scopes;
	}
	public void setScopes(Set<String> scopes) {
		this.scopes = scopes;
	}
	public String getAuthorizationUrl() {
		return authorizationUrl;
	}
	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	
}
