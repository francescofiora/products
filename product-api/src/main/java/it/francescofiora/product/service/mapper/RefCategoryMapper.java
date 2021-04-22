package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.service.dto.RefCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Category} and its Dto {@link RefCategoryDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RefCategoryMapper extends DtoToEntityMapper<RefCategoryDto, Category> {

  @Mapping(target = "products", ignore = true)
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "description", ignore = true)
  Category toEntity(RefCategoryDto categoryDto);

  /**
   * new Category from Id.
   * @param id Long
   * @return Category
   */
  default Category fromId(Long id) {
    if (id == null) {
      return null;
    }
    Category category = new Category();
    category.setId(id);
    return category;
  }
}
