package it.francescofiora.product.api.service.mapper;

import it.francescofiora.product.api.domain.Product;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.RefProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Product Mapper.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

  ProductDto toDto(Product entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "category.id", target = "category")
  Product toEntity(NewProductDto productDto);

  @Mapping(target = "name", ignore = true)
  @Mapping(target = "description", ignore = true)
  @Mapping(target = "price", ignore = true)
  @Mapping(target = "size", ignore = true)
  @Mapping(target = "image", ignore = true)
  @Mapping(target = "imageContentType", ignore = true)
  @Mapping(target = "category", ignore = true)
  Product toEntity(RefProductDto productDto);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UpdatebleProductDto productDto, @MappingTarget Product product);

  /**
   * new Product from Id.
   *
   * @param id Long
   * @return Product
   */
  default Product fromId(Long id) {
    if (id == null) {
      return null;
    }
    var product = new Product();
    product.setId(id);
    return product;
  }
}
