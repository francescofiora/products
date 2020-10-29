package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.OrderItem;
import it.francescofiora.product.service.dto.OrderItemDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link OrderItem} and its Dto {@link OrderItemDto}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper extends EntityToDtoMapper<OrderItemDto, OrderItem> {

}
