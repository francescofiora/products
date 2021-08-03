package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Order;
import it.francescofiora.product.domain.OrderItem;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.OrderDto;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import java.math.BigDecimal;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

/**
 * Order Mapper.
 */
@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

  @Mapping(source = "orderItems", target = "items")
  @Mapping(source = "orderItems", target = "totalPrice", qualifiedByName = "getTotalPrice")
  OrderDto toDto(Order entity);

  /**
   * get TotalPrice.
   *
   * @param orderItems Set
   * @return BigDecimal
   */
  @Named("getTotalPrice")
  public static BigDecimal getTotalPrice(Set<OrderItem> orderItems) {
    var result = new BigDecimal(0);
    for (var item : orderItems) {
      result = result.add(item.getTotalPrice());
    }
    return result;
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(source = "items", target = "orderItems")
  Order toEntity(NewOrderDto orderDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "orderItems", ignore = true)
  void updateEntityFromDto(UpdatebleOrderDto orderDto, @MappingTarget Order order);
}
