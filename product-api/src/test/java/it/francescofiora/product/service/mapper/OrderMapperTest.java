package it.francescofiora.product.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class OrderMapperTest {

  private OrderMapper orderMapper = new OrderMapperImpl();

  @Test
  void testNullObject() {
    assertThat(orderMapper.toDto(null)).isNull();

    assertThat(orderMapper.toEntity(null)).isNull();
 
    assertDoesNotThrow(() -> orderMapper.updateEntityFromDto(null, null));
  }
}
