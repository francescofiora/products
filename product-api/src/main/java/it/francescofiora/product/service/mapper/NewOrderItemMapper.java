package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.OrderItem;
import it.francescofiora.product.service.dto.NewOrderItemDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link OrderItem} and its Dto {@link NewOrderItemDto}.
 */
@Mapper(componentModel = "spring", uses = {RefProductMapper.class})
public interface NewOrderItemMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "order", ignore = true)
  @Mapping(target = "totalPrice", ignore = true)
  OrderItem toEntity(NewOrderItemDto orderItemDto);
}
