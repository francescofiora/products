package it.francescofiora.product.itt.context;

import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Category Context.
 */
@Component
@Getter
@Setter
public class CategoryContext {

  private NewCategoryDto newCategoryDto;
  private CategoryDto updatebleCategoryDto;
  private CategoryDto categoryDto;
  private Long categoryId;
  private List<CategoryDto> categories;

}
