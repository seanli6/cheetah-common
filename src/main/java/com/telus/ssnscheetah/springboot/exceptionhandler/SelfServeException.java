package com.telus.ssnscheetah.springboot.exceptionhandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SelfServeException extends Exception {

	private static final long serialVersionUID = 1L;
	public static final String INTERNAL_SERVER_ERROR="ERR-INTL";
	public static final String DOMAIN_ERROR="ERR-DOMAIN";
	public static final String INVALID_BODY="INVALID-BODY";
	public static final String MISSING_QUERYSTRING_PARAM = "MISSING-PARAM";
	public static final String INVALID_QUERYSTRING_PARAM = "INVALID-PARAM";
	
	public static final String HTTP_STATUS_CODE_200 ="200";
	public static final String HTTP_STATUS_CODE_400 ="400";
	public static final String HTTP_STATUS_CODE_403 ="403";
	public static final String HTTP_STATUS_CODE_500 ="500";
	public static final String HTTP_STATUS_CODE_401 ="401";
	public static final String HTTP_STATUS_CODE_404 ="404";
	public static final String HTTP_STATUS_CODE_405 ="405";
	public static final String HTTP_STATUS_CODE_422 ="422";

	private String code;
	private String reason;
	private String message;
	private String status;
	private String referenceError;
	private String httpStatusCd;
	private String transactionTimestamp;
	private String id;

	public SelfServeException() {

	}

	public SelfServeException(Throwable cause, String code, String reason, String message, String status,
			String referenceError, String httpStatusCd) {
		super(message, cause);
		this.code = code;
		this.reason = reason;
		this.message = message;
		this.status = status;
		this.referenceError = referenceError;
		this.httpStatusCd = httpStatusCd;
		this.transactionTimestamp = createTransactionTimestamp();
		this.id = createUniqueId();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReferenceError() {
		return referenceError;
	}

	public void setReferenceError(String referenceError) {
		this.referenceError = referenceError;
	}

	public String getHttpStatusCd() {
		return httpStatusCd;
	}

	public void setHttpStatusCd(String httpStatusCd) {
		this.httpStatusCd = httpStatusCd;
	}

	public String getTransactionTimestamp() {
		return transactionTimestamp;
	}

	public void setTransactionTimestamp(String transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public static String createUniqueId() {
		try {
			String hostName = InetAddress.getLocalHost().getHostName();
			return hostName + "-" + UUID.randomUUID().toString();
		} catch ( UnknownHostException e ) {
			return UUID.randomUUID().toString();
		}
	}
	
	public static String createTransactionTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(new Date());
	}

	
}