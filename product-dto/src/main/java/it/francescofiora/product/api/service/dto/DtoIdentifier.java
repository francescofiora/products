package it.francescofiora.product.api.service.dto;

import javax.validation.constraints.NotNull;

/**
 * Dto Identifier.
 */
public interface DtoIdentifier {

  /**
   * Get Id.
   *
   * @return the id
   */
  Long getId();

  /**
   * Set Id.
   *
   * @param id the id
   */
  void setId(@NotNull Long id);
}
