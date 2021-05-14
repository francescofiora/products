package it.francescofiora.product.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderItemMapperTest {

  private OrderItemMapper orderItemMapper = new OrderItemMapperImpl();

  @Test
  void testNullObject() {
    assertThat(orderItemMapper.toDto(null)).isNull();

    assertThat(orderItemMapper.toEntity(null)).isNull();
  }
}
