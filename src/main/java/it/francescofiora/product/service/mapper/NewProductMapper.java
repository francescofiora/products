package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Product;
import it.francescofiora.product.service.dto.NewProductDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Product} and its DTO {@link NewProductDto}.
 */
@Mapper(componentModel = "spring", uses = {RefCategoryMapper.class})
public interface NewProductMapper extends DtoToEntityMapper<NewProductDto, Product> {

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "category.id", target = "category")
  Product toEntity(NewProductDto productDto);
}
