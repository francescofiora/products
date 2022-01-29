package it.francescofiora.product.api.service.mapper;

import it.francescofiora.product.api.domain.OrderItem;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * OrderItem Mapper.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

  OrderItemDto toDto(OrderItem entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "order", ignore = true)
  @Mapping(target = "totalPrice", ignore = true)
  OrderItem toEntity(NewOrderItemDto orderItemDto);
}
