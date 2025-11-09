package it.francescofiora.product.api.web.api.impl;

import it.francescofiora.product.api.service.CategoryService;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.web.api.CategoryApi;
import it.francescofiora.product.api.web.errors.BadRequestAlertException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller implementation for managing Category.
 */
@RestController
public class CategoryApiImpl extends AbstractApi implements CategoryApi {

  private static final String ENTITY_NAME = "CategoryDto";

  private final CategoryService categoryService;

  public CategoryApiImpl(CategoryService categoryService) {
    super(ENTITY_NAME);
    this.categoryService = categoryService;
  }

  @Override
  public ResponseEntity<Void> createCategory(NewCategoryDto categoryDto) {
    var result = categoryService.create(categoryDto);
    return postResponse("/api/v1/categories/" + result.getId(), result.getId());
  }

  @Override
  public ResponseEntity<Void> updateCategory(Long id, CategoryDto categoryDto) {
    if (!id.equals(categoryDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(categoryDto.getId()),
          "Invalid id");
    }
    categoryService.update(categoryDto);
    return putResponse(id);
  }

  @Override
  public ResponseEntity<List<CategoryDto>> findCategories(String name, String description,
      Pageable pageable) {
    return getResponse(categoryService.findAll(name, description, pageable));
  }

  @Override
  public ResponseEntity<CategoryDto> getCategoryById(Long id) {
    return getResponse(categoryService.findOne(id), id);
  }

  @Override
  public ResponseEntity<Void> deleteCategory(Long id) {
    categoryService.delete(id);
    return deleteResponse(id);
  }
}
