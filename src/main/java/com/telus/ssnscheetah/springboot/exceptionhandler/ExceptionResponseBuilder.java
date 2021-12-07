package com.telus.ssnscheetah.springboot.exceptionhandler;

import java.util.Iterator;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

public class ExceptionResponseBuilder {

	private final static Log log = LogFactory.getLog(ExceptionResponseBuilder.class);

	private static final String MSG_SERVICE_CALL_FAILED = "Service call failed";
	private static final String EMPTY_STR = "";

	public static ErrorRepresentation processSelfServeException(SelfServeException sse) {
		return ExceptionResponseBuilder.buildStatusVoFromSelfServeException(sse);
	}

	public static ErrorRepresentation processValidationException(ValidationException ve, String serviceName) {

		if (ve.getCause() != null && ve.getCause() instanceof RuntimeException) {
			return processGenericException(ve, serviceName);
		} else {
			return ExceptionResponseBuilder.buildStatusVoFromValidationException(ve);
		}
	}

	public static ErrorRepresentation processHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
		logGenericException(hmnre);
		return ExceptionResponseBuilder.buildStatusVoFromHttpMessageNotReadableException(hmnre);
	}

	public static ErrorRepresentation processMissingServletRequestParamException(
			MissingServletRequestParameterException missingParamException) {
		logGenericException(missingParamException);

		return buildStatusVoFromMissingServletRequestParamException(missingParamException);
	}

	public static ErrorRepresentation processMethodArgNotValidException(MethodArgumentNotValidException manve) {
		return ExceptionResponseBuilder.buildStatusVoFromMethodArgNotValidException(manve);
	}
	
	public static ErrorRepresentation processMethodArgNotValidException(IllegalArgumentException manve) {
		return ExceptionResponseBuilder.buildStatusVoFromIllegalArgumentException(manve);
	}

	public static ErrorRepresentation processGenericException(Exception e, String serviceName) {
		SelfServeException sse = new SelfServeException(e, SelfServeException.INTERNAL_SERVER_ERROR, (e != null?e.getMessage():null),
				(serviceName == null ? EMPTY_STR : serviceName + MSG_SERVICE_CALL_FAILED), null, null, SelfServeException.HTTP_STATUS_CODE_500);
		return processSelfServeException(sse);
	}

	public static ErrorRepresentation buildStatusVoFromSelfServeException(SelfServeException sse) {

		ErrorRepresentation errorRepresentation = new ErrorRepresentation();
		errorRepresentation.setCode(sse.getCode());
		errorRepresentation.setReason(sse.getReason());
		errorRepresentation.setMessage(sse.getMessage());
		errorRepresentation.setStatus(sse.getStatus());
		errorRepresentation.setReferenceError(sse.getReferenceError());
		errorRepresentation.setTransactionTimestamp(sse.getTransactionTimestamp());
		errorRepresentation.setId(sse.getId());
		return errorRepresentation;
	}

	public static ErrorRepresentation buildStatusVoFromValidationException(ValidationException ve) {
		ErrorRepresentation errorRepresentation = new ErrorRepresentation();
		errorRepresentation.setCode(SelfServeException.INVALID_QUERYSTRING_PARAM);
		errorRepresentation.setReason(extractMessageFromValidationException(ve));
		errorRepresentation.setMessage("ValidationException");
		errorRepresentation.setTransactionTimestamp(SelfServeException.createTransactionTimestamp());
		errorRepresentation.setId(SelfServeException.createUniqueId());
		return errorRepresentation;
	}

	public static ErrorRepresentation buildStatusVoFromHttpMessageNotReadableException(
			HttpMessageNotReadableException hmnre) {

		// If the root cause is found to be ValidationException, then process it as
		// ValidationException
		if (hmnre.getMostSpecificCause() != null && hmnre.getMostSpecificCause() instanceof ValidationException) {
			return buildStatusVoFromValidationException((ValidationException) hmnre.getMostSpecificCause());
		}

		ErrorRepresentation errorRepresentation = new ErrorRepresentation();
		errorRepresentation.setCode(SelfServeException.INVALID_BODY);
		errorRepresentation.setReason(extractMessageFromHttpMessageNotReadableException(hmnre));
		errorRepresentation.setReason("HttpMessageNotReadableException");
		errorRepresentation.setTransactionTimestamp(SelfServeException.createTransactionTimestamp());
		errorRepresentation.setId(SelfServeException.createUniqueId());
		return errorRepresentation;
	}

	public static ErrorRepresentation buildStatusVoFromMissingServletRequestParamException(
			MissingServletRequestParameterException missingParamException) {
		ErrorRepresentation errorRepresentation = new ErrorRepresentation();
		errorRepresentation.setCode(SelfServeException.MISSING_QUERYSTRING_PARAM);
		errorRepresentation.setReason(missingParamException.getMessage());
		errorRepresentation.setMessage("MissingServletRequestParamException");
		errorRepresentation.setTransactionTimestamp(SelfServeException.createTransactionTimestamp());
		errorRepresentation.setId(SelfServeException.createUniqueId());
		return errorRepresentation;
	}

	public static ErrorRepresentation buildStatusVoFromMethodArgNotValidException(
			MethodArgumentNotValidException manve) {
		ErrorRepresentation errorRepresentation = new ErrorRepresentation();
		errorRepresentation.setCode(SelfServeException.INVALID_QUERYSTRING_PARAM);
		errorRepresentation.setReason(extractMessageFromMethodArgNotValidException(manve));
		errorRepresentation.setMessage("MethodArgNotValidException");
		errorRepresentation.setTransactionTimestamp(SelfServeException.createTransactionTimestamp());
		errorRepresentation.setId(SelfServeException.createUniqueId());
		return errorRepresentation;
	}
	
	public static ErrorRepresentation buildStatusVoFromIllegalArgumentException(
			IllegalArgumentException ire) {
		ErrorRepresentation errorRepresentation = new ErrorRepresentation();
		errorRepresentation.setCode(SelfServeException.INVALID_QUERYSTRING_PARAM);
		errorRepresentation.setReason(ire.getMessage());
		errorRepresentation.setMessage("IllegalArgumentException");
		errorRepresentation.setTransactionTimestamp(SelfServeException.createTransactionTimestamp());
		errorRepresentation.setId(SelfServeException.createUniqueId());
		return errorRepresentation;
	}

	// ------------------------------------------------------------------------
	// Private methods
	// ------------------------------------------------------------------------

	/*
	 * Extracts friendly error message from Validation exception.
	 */
	private static String extractMessageFromValidationException(ValidationException ve) {

		StringBuffer sb_exception = new StringBuffer();
		sb_exception.append("validation errors: {");
		if (ve instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) ve;
			if (cve.getConstraintViolations() != null) {
				Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
				Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
				while (iterator.hasNext()) {
					ConstraintViolation<?> violation = iterator.next();
					sb_exception.append("[" + violation.getMessage() + "]");
					if (iterator.hasNext())
						sb_exception.append(", ");
					else
						sb_exception.append("}");
				}
			}

		} else {
			sb_exception.append("[").append(ve.getMessage()).append("]}");
		}

		return sb_exception.toString();
	}

	private static String extractMessageFromHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
		String errMsg = null;
		if (hmnre.getMostSpecificCause() != null) {
			errMsg = hmnre.getMostSpecificCause().getClass().getCanonicalName() + " : "
					+ hmnre.getMostSpecificCause().getMessage();
		} else {
			errMsg = hmnre.getMessage();
		}
		return errMsg;
	}

	private static String extractMessageFromMethodArgNotValidException(MethodArgumentNotValidException manve) {

		StringBuffer sb_exception = new StringBuffer();
		sb_exception.append("validation errors: {");

		BindingResult result = manve.getBindingResult();

		int count = 0;
		// process the field validations
		for (FieldError fieldError : result.getFieldErrors()) {
			if (count++ > 0) {
				sb_exception.append(", ");
			}
			sb_exception.append("[" + fieldError.getDefaultMessage() + "]");
		}

		// process the global validations
		for (ObjectError globalError : result.getGlobalErrors()) {
			if (count++ > 0) {
				sb_exception.append(", ");
			}
			sb_exception.append("[" + globalError.getDefaultMessage() + "]");
		}
		sb_exception.append("}");

		return sb_exception.toString();
	}

	private static void logGenericException(Exception e) {
		log.error("Exception Handler Log Entry:", e);
	}

}
