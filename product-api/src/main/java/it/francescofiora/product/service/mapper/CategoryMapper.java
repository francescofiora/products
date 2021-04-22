package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.service.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Category} and its Dto {@link CategoryDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "products", ignore = true)
  void updateEntityFromDto(CategoryDto categoryDto, @MappingTarget Category category);

  CategoryDto toDto(Category entity);
}
