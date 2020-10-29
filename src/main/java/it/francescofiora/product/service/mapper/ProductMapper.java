package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Product;
import it.francescofiora.product.service.dto.ProductDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Product} and its Dto {@link ProductDto}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper extends EntityToDtoMapper<ProductDto, Product> {

}
