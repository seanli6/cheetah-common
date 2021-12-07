package com.telus.ssnscheetah.springboot.kongconsumer.oauth2;

public interface OAuth2ParamNames {
	/**
	 * {@code grant_type} - used in Access Token Request.
	 */
	String GRANT_TYPE = "grant_type";

	/**
	 * {@code client_id} - used in Access Token Request.
	 */
	String CLIENT_ID = "client_id";

	/**
	 * {@code client_secret} - used in Access Token Request.
	 */
	String CLIENT_SECRET = "client_secret";

	/**
	 * {@code redirect_uri} - used in Access Token Request.
	 */
	String REDIRECT_URI = "redirect_uri";

	/**
	 * {@code scope} - used in  Access Token Request and Access Token Response.
	 */
	String SCOPE = "scope";

	/**
	 * {@code code} - used in  Access Token Request.
	 */
	String CODE = "code";

	/**
	 * {@code access_token} - used in Access Token Response.
	 */
	String ACCESS_TOKEN = "access_token";

	/**
	 * {@code token_type} - used in  Access Token Response.
	 */
	String TOKEN_TYPE = "token_type";

	/**
	 * {@code expires_in} - used in  Access Token Response.
	 */
	String EXPIRES_IN = "expires_in";

	/**
	 * {@code refresh_token} - used in Access Token Request .
	 */
	String REFRESH_TOKEN = "refresh_token";

	/**
	 * {@code username} - used in Access Token Request.
	 */
	String USERNAME = "username";

	/**
	 * {@code password} - used in Access Token Request.
	 */
	String PASSWORD = "password";

	/**
	 * {@code error} - used in Authorization Response and Access Token Response.
	 */
	String ERROR = "error";

	/**
	 * {@code error_description} - used in Authorization Response and Access Token Response.
	 */
	String ERROR_DESCRIPTION = "error_description";

	/**
	 * {@code error_uri} - used in Authorization Response and Access Token Response.
	 */
	String ERROR_URI = "error_uri";

}
