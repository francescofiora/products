package it.francescofiora.product.api.service.mapper;

import it.francescofiora.product.api.domain.Category;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Category Mapper.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "products", ignore = true)
  void updateEntityFromDto(CategoryDto categoryDto, @MappingTarget Category category);

  CategoryDto toDto(Category entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "products", ignore = true)
  Category toEntity(NewCategoryDto categoryDto);

  @Mapping(target = "products", ignore = true)
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "description", ignore = true)
  Category toEntity(RefCategoryDto categoryDto);

  /**
   * new Category from Id.
   *
   * @param id Long
   * @return Category
   */
  default Category fromId(Long id) {
    if (id == null) {
      return null;
    }
    var category = new Category();
    category.setId(id);
    return category;
  }
}
