package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Order;
import it.francescofiora.product.service.dto.NewOrderDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Order} and its Dto {@link NewOrderDto}.
 */
@Mapper(componentModel = "spring", uses = {NewOrderItemMapper.class})
public interface NewOrderMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(source = "items", target = "orderItems")
  Order toEntity(NewOrderDto orderDto);
}
