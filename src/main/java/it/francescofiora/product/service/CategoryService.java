package it.francescofiora.product.service;

import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

  /**
   * Create a new category.
   *
   * @param categoryDto the entity to create.
   * @return the persisted entity.
   */
  CategoryDto create(NewCategoryDto categoryDto);

  /**
   * Update a category.
   *
   * @param categoryDto the entity to update.
   */
  void update(CategoryDto categoryDto);

  /**
   * Get all the categories.
   * @param pageable Pageable
   * @return the list of entities.
   */
  Page<CategoryDto> findAll(Pageable pageable);

  /**
   * Get the "id" category.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<CategoryDto> findOne(Long id);

  /**
   * Delete the "id" category.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);
}
