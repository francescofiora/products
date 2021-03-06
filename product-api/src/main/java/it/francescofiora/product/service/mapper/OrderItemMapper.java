package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.OrderItem;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

  OrderItemDto toDto(OrderItem entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "order", ignore = true)
  @Mapping(target = "totalPrice", ignore = true)
  OrderItem toEntity(NewOrderItemDto orderItemDto);
}
