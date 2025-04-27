package it.francescofiora.product.company.api.web.errors;

import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Not Found Alert Exception.
 */
public class NotFoundAlertException extends ResponseStatusException {

  @Serial
  private static final long serialVersionUID = 1L;

  @Getter
  private final String entityName;

  @Getter
  private final String errorKey;

  /**
   * Constructor.
   *
   * @param entityName entity Name
   * @param errorKey the errorKey
   * @param errorMessage message
   */
  public NotFoundAlertException(String entityName, String errorKey, String errorMessage) {
    super(HttpStatus.NOT_FOUND, errorMessage);
    this.entityName = entityName;
    this.errorKey = errorKey;
  }
}
