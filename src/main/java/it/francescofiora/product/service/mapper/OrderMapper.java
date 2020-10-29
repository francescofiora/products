package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Order;
import it.francescofiora.product.domain.OrderItem;
import it.francescofiora.product.service.dto.OrderDto;
import java.math.BigDecimal;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Order} and its Dto {@link OrderDto}.
 */
@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper extends EntityToDtoMapper<OrderDto, Order> {

  @Mapping(source = "orderItems", target = "items")
  @Mapping(source = "orderItems", target = "totalPrice", qualifiedByName = "getTotalPrice")
  OrderDto toDto(Order entity);

  /**
   * get TotalPrice.
   * @param orderItems Set
   * @return BigDecimal
   */
  @Named("getTotalPrice")
  public static BigDecimal getTotalPrice(Set<OrderItem> orderItems) {
    BigDecimal result = new BigDecimal(0);
    for (OrderItem item : orderItems) {
      result = result.add(item.getTotalPrice());
    }
    return result;
  }
}
