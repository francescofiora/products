package it.francescofiora.product.itt.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.francescofiora.product.client.CategoryApiService;
import it.francescofiora.product.itt.context.CategoryContext;
import it.francescofiora.product.itt.util.TestProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Category Component.
 */
@Component
@RequiredArgsConstructor
public class CategoryComponent extends AbstractComponent {

  private final CategoryContext categoryContext;
  private final CategoryApiService categoryApiService;

  public void createNewCategoryDto(String name, String description) {
    categoryContext.setNewCategoryDto(TestProductUtils.createNewCategoryDto(name, description));
  }

  public void createCategory() {
    var result = categoryApiService.createCategory(categoryContext.getNewCategoryDto());
    categoryContext.setCategoryId(validateResponseAndGetId(result));
  }

  /**
   * Fetch Category.
   */
  public void fetchCategory() {
    var result = categoryApiService.getCategoryById(categoryContext.getCategoryId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    categoryContext.setCategoryDto(result.getBody());
  }

  /**
   * Update Category.
   *
   * @param name the name
   * @param description the description
   */
  public void updateCategory(String name, String description) {
    var categoryId = categoryContext.getCategoryId();
    categoryContext.setUpdatebleCategoryDto(
        TestProductUtils.createCategoryDto(categoryId, name, description));
    var result = categoryApiService
        .updateCategory(categoryContext.getUpdatebleCategoryDto(), categoryId);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  public void deleteCategory() {
    var result = categoryApiService.deleteCategoryById(categoryContext.getCategoryId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Fetch all Categories.
   */
  public void fetchAllCategories() {
    var result = categoryApiService.findCategories(null, null, Pageable.unpaged());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotEmpty();
    categoryContext.setCategories(result.getBody());
  }

  /**
   * Compare CategoryDto with NewCategoryDto.
   */
  public void compareCategoryWithNewCategory() {
    var category = categoryContext.getCategoryDto();
    assertNotNull(category);
    var newCategoryDto = categoryContext.getNewCategoryDto();
    assertNotNull(newCategoryDto);
    assertThat(category.getName()).isEqualTo(newCategoryDto.getName());
    assertThat(category.getDescription()).isEqualTo(newCategoryDto.getDescription());
  }

  /**
   * Compare UpdatebleCategoryDto into Categories.
   */
  public void compareUpdatebleCategoryIntoCategories() {
    var categoryId = categoryContext.getCategoryId();
    var opt = categoryContext.getCategories().stream()
        .filter(cat -> categoryId.equals(cat.getId())).findAny();
    assertThat(opt).isNotEmpty();
    assertThat(opt).hasValue(categoryContext.getUpdatebleCategoryDto());
  }

  /**
   * Check Category Not Exist.
   */
  public void checkCategoryNotExist() {
    var resultCat = categoryApiService.findCategories(null, null, Pageable.unpaged());
    assertThat(resultCat.getBody()).isNotNull();
    categoryContext.setCategories(resultCat.getBody());
    var categoryId = categoryContext.getCategoryId();
    var opt = categoryContext.getCategories().stream()
        .filter(cat -> categoryId.equals(cat.getId())).findAny();
    assertThat(opt).isEmpty();
  }
}
