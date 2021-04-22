package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.service.dto.NewCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Category} and its Dto {@link NewCategoryDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NewCategoryMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "products", ignore = true)
  Category toEntity(NewCategoryDto categoryDto);
}
