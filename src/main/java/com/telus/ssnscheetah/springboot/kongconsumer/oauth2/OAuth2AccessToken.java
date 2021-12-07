package com.telus.ssnscheetah.springboot.kongconsumer.oauth2;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OAuth2AccessToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
	private String tokenType;
	private LocalDateTime expiresDt;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiresDt() {
		return expiresDt;
	}
	public void setExpiresDt(LocalDateTime expiresAt) {
		this.expiresDt = expiresAt;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	
}
