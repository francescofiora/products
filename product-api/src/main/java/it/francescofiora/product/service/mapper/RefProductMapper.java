package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Product;
import it.francescofiora.product.service.dto.RefProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Ref Mapper for the entity {@link Product} and its Dto {@link RefProductDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RefProductMapper extends DtoToEntityMapper<RefProductDto, Product> {

  @Mapping(target = "name", ignore = true)
  @Mapping(target = "description", ignore = true)
  @Mapping(target = "price", ignore = true)
  @Mapping(target = "size", ignore = true)
  @Mapping(target = "image", ignore = true)
  @Mapping(target = "imageContentType", ignore = true)
  @Mapping(target = "category", ignore = true)
  Product toEntity(RefProductDto productDto);

  /**
   * new Product from Id.
   * @param id Long
   * @return Product
   */
  default Product fromId(Long id) {
    if (id == null) {
      return null;
    }
    Product product = new Product();
    product.setId(id);
    return product;
  }
}
