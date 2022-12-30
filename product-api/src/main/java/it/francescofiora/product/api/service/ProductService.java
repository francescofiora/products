package it.francescofiora.product.api.service;

import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link it.francescofiora.product.domain.Product}.
 */
public interface ProductService {

  String ENTITY_NAME = "ProductDto";
  String PRODUCT_NOT_FOUND = "Product not found";

  /**
   * Create a product.
   *
   * @param productDto the entity to create
   * @return the persisted entity
   */
  ProductDto create(NewProductDto productDto);

  /**
   * Update a product.
   *
   * @param productDto the entity to update
   */
  void update(UpdatebleProductDto productDto);

  /**
   * Get all the products.
   *
   * @param name the name
   * @param description the description
   * @param categoryId the id of the category
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<ProductDto> findAll(String name, String description, Long categoryId, Pageable pageable);

  /**
   * Get the "id" product.
   *
   * @param id the id of the entity
   * @return the entity
   */
  Optional<ProductDto> findOne(Long id);

  /**
   * Delete the "id" product.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
