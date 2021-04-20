package it.francescofiora.product.service;

import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.ProductDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link it.francescofiora.product.domain.Product}.
 */
public interface ProductService {

  /**
   * Create a product.
   *
   * @param productDto the entity to create.
   * @return the persisted entity.
   */
  ProductDto create(NewProductDto productDto);

  /**
   * Update a product.
   *
   * @param productDto the entity to update.
   */
  void update(UpdatebleProductDto productDto);

  /**
   * Get all the products.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<ProductDto> findAll(Pageable pageable);

  /**
   * Get the "id" product.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<ProductDto> findOne(Long id);

  /**
   * Delete the "id" product.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
