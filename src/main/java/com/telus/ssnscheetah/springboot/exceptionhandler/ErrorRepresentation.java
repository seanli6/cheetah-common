package com.telus.ssnscheetah.springboot.exceptionhandler;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard tmf error representation
 */

@Validated

public class ErrorRepresentation   {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("reason")
  private String reason = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("referenceError")
  private String referenceError = null;

  @JsonProperty("@type")
  private String type = null;

  @JsonProperty("@schemaLocation")
  private String schemaLocation = null;
  
  @JsonProperty("transactionTimestamp")
  private String transactionTimestamp = null;
  
  @JsonProperty("id")
  private String id = null;

  public ErrorRepresentation code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Application related code (as defined in the API or from a common list)
   * @return code
  **/
  @NotNull
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ErrorRepresentation reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Text that explains the reason for error. This can be shown to a client user.
   * @return reason
  **/
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public ErrorRepresentation message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Text that provides more details and corrective actions related to the error. This can be shown to a client user.
   * @return message
  **/
  @NotNull

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ErrorRepresentation status(String status) {
    this.status = status;
    return this;
  }

  /**
   * http error code extension like 400-2
   * @return status
  **/
  

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ErrorRepresentation referenceError(String referenceError) {
    this.referenceError = referenceError;
    return this;
  }

  /**
   * url pointing to documentation describing the error
   * @return referenceError
  **/

  public String getReferenceError() {
    return referenceError;
  }

  public void setReferenceError(String referenceError) {
    this.referenceError = referenceError;
  }

  public ErrorRepresentation type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Indicates the type of resource
   * @return type
  **/

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ErrorRepresentation schemaLocation(String schemaLocation) {
    this.schemaLocation = schemaLocation;
    return this;
  }

  /**
   * Link to schema describing this REST resource
   * @return schemaLocation
  **/

  public String getSchemaLocation() {
    return schemaLocation;
  }

  public void setSchemaLocation(String schemaLocation) {
    this.schemaLocation = schemaLocation;
  }

  public ErrorRepresentation transactionTimestamp(String transactionTimestamp) {
    this.transactionTimestamp = transactionTimestamp;
    return this;
  }
  
  /**
   * Time stamp of when the error occurred
   * @return transactionTimestamp
   */
  
  public String getTransactionTimestamp() {
    return transactionTimestamp;
  }
	
  public void setTransactionTimestamp(String transactionTimestamp) {
	this.transactionTimestamp = transactionTimestamp;
  }
	
  public ErrorRepresentation id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Required, added by TELUS. A unique error id for this instance of the error.
   * @return id
   */
  public String getId() {
    return id;
  }
	
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorRepresentation errorRepresentation = (ErrorRepresentation) o;
    return Objects.equals(this.code, errorRepresentation.code) &&
        Objects.equals(this.reason, errorRepresentation.reason) &&
        Objects.equals(this.message, errorRepresentation.message) &&
        Objects.equals(this.status, errorRepresentation.status) &&
        Objects.equals(this.referenceError, errorRepresentation.referenceError) &&
        Objects.equals(this.type, errorRepresentation.type) &&
        Objects.equals(this.schemaLocation, errorRepresentation.schemaLocation) &&
    	Objects.equals(this.transactionTimestamp, errorRepresentation.transactionTimestamp) &&
    	Objects.equals(this.id, errorRepresentation.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, reason, message, status, referenceError, type, schemaLocation, transactionTimestamp, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorRepresentation {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    referenceError: ").append(toIndentedString(referenceError)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    schemaLocation: ").append(toIndentedString(schemaLocation)).append("\n");
    sb.append("    transactionTimestamp: ").append(toIndentedString(transactionTimestamp)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

