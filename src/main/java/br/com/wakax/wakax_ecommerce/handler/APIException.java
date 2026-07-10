package br.com.wakax.wakax_ecommerce.handler;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class APIException extends RuntimeException {
  private HttpStatus statusException;
  private ErrorApiResponse bodyException;
  private ErrorCode errorCode;
  private Object[] args;

  public APIException(HttpStatus statusException, ErrorCode errorCode, Object... args) {
    super(errorCode != null ? errorCode.getCode() : null);
    this.statusException = statusException;
    this.errorCode = errorCode;
    this.args = args;
    this.bodyException = null; // será montado no handler
  }

  public APIException(HttpStatus statusException, ErrorCode errorCode, Exception e) {
    super(errorCode != null ? errorCode.getCode() : null, e);
    this.statusException = statusException;
    this.errorCode = errorCode;
    this.bodyException = null;
  }

  private APIException(HttpStatus statusException, String message, Exception e) {
    super(message, e);
    this.statusException = statusException;
    this.bodyException =
        ErrorApiResponse.builder().message(message).description(getDescription(e)).build();
  }

  public static APIException build(HttpStatus statusException, String message) {
    return new APIException(statusException, message, null);
  }

  public static APIException build(HttpStatus statusException, String message, Exception e) {
    log.error("Exception: ", e);
    return new APIException(statusException, message, e);
  }

  private String getDescription(Exception e) {
    return Optional.ofNullable(e).map(APIException::getMessageCause).orElse(null);
  }

  private static String getMessageCause(Exception e) {
    return e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
  }

  public ResponseEntity<ErrorApiResponse> buildErrorResponseEntity() {
    return ResponseEntity.status(statusException).body(bodyException);
  }

  private static final long serialVersionUID = 1L;
}
