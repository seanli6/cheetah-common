package com.telus.ssnscheetah.springboot.kongconsumer;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.ssnscheetah.springboot.kongconsumer.oauth2.KongRestTemplateInterceptor;
import com.telus.ssnscheetah.springboot.kongconsumer.oauth2.OAuth2AuthorizationGrantRequest;

public class KongRestClient  {
	

	private com.telus.ssnscheetah.springboot.kongconsumer.oauth2.OAuth2AuthorizationGrantRequest oAuth2AuthorizationGrantRequest;

	private String baseUrl;
	private RestTemplate restTemplate;
	private String defaultDateFormat;
	private SimpleDateFormat dateFormatter;
	private CloseableHttpClient httpClient;
	private int maxTotalConnections = 600;
	private int maxConnections = 400;
	

	public RestTemplate createRestTemplate () throws Exception {
		

		URI serviceURI = new URI(baseUrl);
		HttpHost targetHost = new HttpHost(serviceURI.getHost(), serviceURI.getPort(), serviceURI.getScheme());
		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());		
		
		final HttpClientContext context = HttpClientContext.create();
		context.setAuthCache(authCache);
		
		
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.getSocketFactory())
        .register("https", new SSLConnectionSocketFactory(getNoCertValidatingSSL(), SSLConnectionSocketFactory.getDefaultHostnameVerifier()))
        .build();
		
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		
		connectionManager.setMaxTotal(getMaxTotalConnections());
		connectionManager.setDefaultMaxPerRoute(getMaxConnections());
		

		
		httpClient = HttpClients
			.custom()
			.setConnectionManager(connectionManager)
			.build();
		
		restTemplate = new RestTemplate();

		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient) {
        	@Override
        	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        		return context;
        	}
        });
        
        if(defaultDateFormat == null){
        	return restTemplate;
        }
        
        List<HttpMessageConverter<?>> list = getRestTemplate().getMessageConverters();
        
        for(HttpMessageConverter<?> converter: list){
        	if(converter instanceof MappingJackson2HttpMessageConverter){
        		MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
        		ObjectMapper objectMapper =  jsonConverter.getObjectMapper();
        		this.dateFormatter = new SimpleDateFormat(defaultDateFormat);
        		objectMapper.setDateFormat(this.dateFormatter);
        		
        	}
        }
		return restTemplate;
	}
	
	private SSLContext getNoCertValidatingSSL() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager passThroughTrustManager = new X509TrustManager(){

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
        };
	
        SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { passThroughTrustManager }, new SecureRandom());
		return sslContext;
	}
	
	@PreDestroy
	public void closeRestClient() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
	}
	
	public String formatDate(Date date) {
		String result = null;
		if(date != null){
			result = this.dateFormatter.format(date);
		}
		return result;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	public String getDefaultDateFormat() {
		return defaultDateFormat;
	}
	public void setDefaultDateFormat(String defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
	public OAuth2AuthorizationGrantRequest getoAuth2AuthorizationGrantRequest() {
		return oAuth2AuthorizationGrantRequest;
	}

	public void setoAuth2AuthorizationGrantRequest(OAuth2AuthorizationGrantRequest oAuth2AuthorizationGrantRequest) {
		this.oAuth2AuthorizationGrantRequest = oAuth2AuthorizationGrantRequest;
	}
	
	public RestTemplate getRestTemplate() throws Exception {
		
		if (restTemplate == null) {
			restTemplate = createRestTemplate();
			List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
			if (interceptors == null) {   // defensively initialize list if it is null. 
				interceptors = new ArrayList<ClientHttpRequestInterceptor>();
				restTemplate.setInterceptors(interceptors);
			}
			interceptors.add(new KongRestTemplateInterceptor(oAuth2AuthorizationGrantRequest));

		}

		return restTemplate;
	}
	
}
