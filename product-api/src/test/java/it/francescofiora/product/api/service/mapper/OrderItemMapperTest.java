package it.francescofiora.product.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderItemMapperTest {

  @Test
  void testNullObject() {
    var orderItemMapper = new OrderItemMapperImpl();
    assertThat(orderItemMapper.toDto(null)).isNull();

    assertThat(orderItemMapper.toEntity(null)).isNull();
  }
}
