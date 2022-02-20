package it.francescofiora.product.client;

import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Product Client Service.
 */
public interface ProductClientService {
  
  /**
   * Create a product.
   *
   * @param productDto the entity to create.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> create(NewProductDto productDto);

  /**
   * Update a product.
   *
   * @param productDto the entity to update.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> update(UpdatebleProductDto productDto);

  /**
   * Get all the products.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Flux<ProductDto> findAll(Pageable pageable);

  /**
   * Get the product by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Mono<ResponseEntity<ProductDto>> findOne(Long id);

  /**
   * Delete the product by id.
   *
   * @param id the id of the entity.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> delete(Long id);
}
