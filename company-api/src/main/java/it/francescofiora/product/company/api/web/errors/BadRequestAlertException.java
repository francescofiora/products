package it.francescofiora.product.company.api.web.errors;

import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Bad Request Exception.
 */
public class BadRequestAlertException extends ResponseStatusException {

  @Serial
  private static final long serialVersionUID = 1L;

  @Getter
  private final String entityName;

  @Getter
  private final String errorKey;

  /**
   * Constructor.
   *
   * @param entityName entity name
   * @param errorKey the errorKey
   * @param errorMessage message
   */
  public BadRequestAlertException(String entityName, String errorKey, String errorMessage) {
    super(HttpStatus.BAD_REQUEST, errorMessage);
    this.entityName = entityName;
    this.errorKey = errorKey;
  }
}
