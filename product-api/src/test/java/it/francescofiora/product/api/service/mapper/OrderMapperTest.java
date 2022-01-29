package it.francescofiora.product.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.api.domain.Order;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import org.junit.jupiter.api.Test;

class OrderMapperTest {

  private OrderMapper orderMapper = new OrderMapperImpl();

  @Test
  void testNullObject() {
    assertThat(orderMapper.toDto(null)).isNull();

    assertThat(orderMapper.toEntity(null)).isNull();

    assertDoesNotThrow(() -> orderMapper.updateEntityFromDto(null, null));

    var order = new Order();
    order.setOrderItems(null);
    var dto = orderMapper.toDto(order);
    assertThat(dto.getTotalPrice()).isNull();
    assertThat(dto.getItems()).isNull();

    var ndto = new NewOrderDto();
    ndto.setItems(null);
    assertThat(orderMapper.toEntity(ndto).getOrderItems()).isNull();
  }
}
