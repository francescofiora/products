package it.francescofiora.product.api.service.dto;

import javax.validation.constraints.NotNull;

/**
 * Dto Identifier.
 */
public interface DtoIdentifier {
  Long getId();

  void setId(@NotNull Long id);
}
