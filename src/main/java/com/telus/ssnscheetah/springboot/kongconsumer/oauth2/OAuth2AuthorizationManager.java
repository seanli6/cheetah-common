package com.telus.ssnscheetah.springboot.kongconsumer.oauth2;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public final class OAuth2AuthorizationManager {
	
	private final Log log = LogFactory.getLog(OAuth2AuthorizationManager.class);
	
	private static OAuth2AccessToken ACCESS_TOKEN_CACHE;
	public static final int timeoutBuffer = 5; //TODO configurable 
//	ReentrantLock lock = new ReentrantLock();

	private static OAuth2AuthorizationManager INSTANCE = new OAuth2AuthorizationManager();
	
	private OAuth2AuthorizationManager() {
		
	}
	
	public static OAuth2AuthorizationManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OAuth2AuthorizationManager();
		}
		return INSTANCE;
	}
	public String getAuthorizationToken(OAuth2AuthorizationGrantRequest oAuth2GrantRequest)  {
	
		if ((ACCESS_TOKEN_CACHE == null) || (LocalDateTime.now().isAfter(ACCESS_TOKEN_CACHE.getExpiresDt()))) {
/*			lock.lock(); 		
			try {
				if ((ACCESS_TOKEN_CACHE == null) || (LocalDateTime.now().isAfter(ACCESS_TOKEN_CACHE.getExpiresDt()))) {//Double check pattern
*/					OAuth2AccessToken newAccessToken = getTokenResponse(oAuth2GrantRequest );
					ACCESS_TOKEN_CACHE = newAccessToken;
/*				}
			} finally {
				lock.unlock();
			}
*/		}

		return  ACCESS_TOKEN_CACHE.getToken();
	}

	public OAuth2AccessToken getTokenResponse(OAuth2AuthorizationGrantRequest oAuth2GrantRequest) {

		OAuth2AccessToken accessToken=null;


	
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<?> request = convert(oAuth2GrantRequest);

			
			ResponseEntity<AuthorizationResponse> responseEntity;
			
			responseEntity = restTemplate.exchange(oAuth2GrantRequest.getAuthorizationUrl(), HttpMethod.POST, request, AuthorizationResponse.class);
			AuthorizationResponse authResponse;
			if (responseEntity != null ) {
				authResponse = responseEntity.getBody();
				accessToken = new OAuth2AccessToken();
				accessToken.setToken(authResponse.getAccess_token());
				accessToken.setTokenType(authResponse.getToken_type());
				accessToken.setExpiresDt( // Set access token expiry time by adding seconds from the response minus the buffer setting.
						LocalDateTime.now().plusSeconds((Long.parseLong(responseEntity.getBody().getExpires_in())) - timeoutBuffer));

			}

		return accessToken;
	}
	
	public HttpEntity<?> convert(OAuth2AuthorizationGrantRequest clientCredentialsGrantRequest) {
	
		
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(org.apache.http.HttpHeaders.AUTHORIZATION, "Basic " + encodeBasicAuth(clientCredentialsGrantRequest.getClientId(), clientCredentialsGrantRequest.getClientSecret()));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


		MultiValueMap<String, String> formParameters = this.buildFormParameters(clientCredentialsGrantRequest);


		return new HttpEntity<>(formParameters, headers);
	}

	/**
	 * Returns a {@link MultiValueMap} of the form parameters used for the Access Token Request body.
	 *
	 * @param clientCredentialsGrantRequest the client credentials grant request
	 * @return a {@link MultiValueMap} of the form parameters used for the Access Token Request body
	 */
	private MultiValueMap<String, String> buildFormParameters(OAuth2AuthorizationGrantRequest clientCredentialsGrantRequest) {

		MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
		formParameters.add(OAuth2ParamNames.GRANT_TYPE, clientCredentialsGrantRequest.getAuthorizationGrantType());
		if (!CollectionUtils.isEmpty(clientCredentialsGrantRequest.getScopes())) {
			formParameters.add(OAuth2ParamNames.SCOPE,
					collectionToDelimitedString(clientCredentialsGrantRequest.getScopes(), " "));
		}
		

		return formParameters;
	}
	
	private String collectionToDelimitedString(Set<String> coll, String delim) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			String anItem = (String)it.next();			
			sb.append(StringUtils.trim(anItem));
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}
	
	private  String encodeBasicAuth(String username, String password) {
		Assert.notNull(username, "Username must not be null");
		Assert.doesNotContain(username, ":", "Username must not contain a colon");
		Assert.notNull(password, "Password must not be null");
		Charset charset = StandardCharsets.ISO_8859_1;

		CharsetEncoder encoder = charset.newEncoder();
		if (!encoder.canEncode(username) || !encoder.canEncode(password)) {
			throw new IllegalArgumentException(
					"Username or password contains characters that cannot be encoded to " + charset.displayName());
		}

		String credentialsString = username + ":" + password;
		byte[] encodedBytes = Base64.getEncoder().encode(credentialsString.getBytes(charset));
		return new String(encodedBytes, charset);
	}	
}
	
