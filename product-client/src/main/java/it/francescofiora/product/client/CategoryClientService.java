package it.francescofiora.product.client;

import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Category Client Service.
 */
public interface CategoryClientService {

  /**
   * Create a category.
   *
   * @param categoryDto the entity to create.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> create(NewCategoryDto categoryDto);

  /**
   * Update a category.
   *
   * @param categoryDto the entity to update.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> update(CategoryDto categoryDto);

  /**
   * Get all the categories.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Flux<CategoryDto> findAll(Pageable pageable);

  /**
   * Get the category by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Mono<ResponseEntity<CategoryDto>> findOne(Long id);

  /**
   * Delete a category by id.
   *
   * @param id the id of the entity.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> delete(Long id);
}
