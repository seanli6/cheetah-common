package com.telus.ssnscheetah.springboot.exceptionhandler;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * ExceptionHandler used in Spring MVC for REST services. It covers all exception type (business exception(SelfServe) , 
 * validation and unexpected Java Exception. 
 * <p>
 * In order to enable this class, we need to include component-scan of this package in the context of the REST service as below:
 * <p>   <context:component-scan base-package="com.telus.selfserve.services.rest.common.exceptionhandler" />	
 * <p>
 * <p> <b>serviceName</b> will be set by value appId from appContext.properties
 * <p>
 * <p>
 *
 */

@ControllerAdvice
public class RestExceptionHandler {
		
	@Value("test") //${fw_appId}
	private String serviceName;
			
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorRepresentation> buildErrorResponseFromHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
		ErrorRepresentation errorRepresentation = ExceptionResponseBuilder.processHttpMessageNotReadableException(hmnre);
		return buildErrorResponseFromStatusVO(errorRepresentation, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorRepresentation> buildErrorResponseFromMissingSerletRequestException(MissingServletRequestParameterException missingParamException) {
		ErrorRepresentation errorRepresentation = ExceptionResponseBuilder.processMissingServletRequestParamException(missingParamException);
		return buildErrorResponseFromStatusVO(errorRepresentation, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SelfServeException.class)
	public ResponseEntity<ErrorRepresentation> buildErrorResponseFromSelfServeException(SelfServeException sse) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorRepresentation errorRepresentation = ExceptionResponseBuilder.processSelfServeException(sse);
		if (SelfServeException.HTTP_STATUS_CODE_400.equals(sse.getHttpStatusCd())) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}else if (SelfServeException.HTTP_STATUS_CODE_401.equals(sse.getHttpStatusCd())) {
			httpStatus = HttpStatus.UNAUTHORIZED;
		}else if (SelfServeException.HTTP_STATUS_CODE_403.equals(sse.getHttpStatusCd())) {
			httpStatus = HttpStatus.FORBIDDEN;
		} else if (SelfServeException.HTTP_STATUS_CODE_404.equals(sse.getHttpStatusCd())) {
			httpStatus = HttpStatus.NOT_FOUND;
		} else if (SelfServeException.HTTP_STATUS_CODE_405.equals(sse.getHttpStatusCd())) {
				httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
		} else if (SelfServeException.HTTP_STATUS_CODE_422.equals(sse.getHttpStatusCd())) {
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		}
		return buildErrorResponseFromStatusVO(errorRepresentation,httpStatus);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorRepresentation> buildErrorResponseFromValidationException(ValidationException ve) {
		ErrorRepresentation errorRepresentation = ExceptionResponseBuilder.processValidationException(ve, serviceName);
		return buildErrorResponseFromStatusVO(errorRepresentation, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorRepresentation> buildErrorResponseFromMethodArgNotValidException(MethodArgumentNotValidException manve) {
		ErrorRepresentation errorRepresentation = ExceptionResponseBuilder.processMethodArgNotValidException(manve);
		return buildErrorResponseFromStatusVO(errorRepresentation, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorRepresentation> buildErrorResponseFromIllegalArgumentException(IllegalArgumentException ire) {
		ErrorRepresentation errorRepresentation = ExceptionResponseBuilder.processMethodArgNotValidException(ire);
		return buildErrorResponseFromStatusVO(errorRepresentation, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRepresentation> buildErrorResponseFromGenericException(Exception e) {
		ErrorRepresentation errorRepresentation = ExceptionResponseBuilder.processGenericException(e, serviceName);
		return buildErrorResponseFromStatusVO(errorRepresentation, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<ErrorRepresentation> buildErrorResponseFromStatusVO(ErrorRepresentation errorRepresentation, HttpStatus status) {
		
		ResponseEntity<ErrorRepresentation> responseEntity = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=UTF-8");
		responseEntity = new ResponseEntity<>(errorRepresentation, headers, status);
		return responseEntity;
	}
	
}
