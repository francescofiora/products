package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Product;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Product} and its Dto {@link UpdatebleProductDto}.
 */
@Mapper(componentModel = "spring", uses = {RefCategoryMapper.class})
public interface UpdatebleProductMapper {

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UpdatebleProductDto productDto, @MappingTarget Product product);
}
