package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.service.util.TestUtils;
import org.junit.jupiter.api.Test;

public class OrderDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtils.equalsVerifier(OrderDto.class);
    OrderDto orderDto1 = new OrderDto();
    orderDto1.setId(1L);
    OrderDto orderDto2 = new OrderDto();
    assertThat(orderDto1).isNotEqualTo(orderDto2);
    orderDto2.setId(orderDto1.getId());
    assertThat(orderDto1).isEqualTo(orderDto2);
    orderDto2.setId(2L);
    assertThat(orderDto1).isNotEqualTo(orderDto2);
    orderDto1.setId(null);
    assertThat(orderDto1).isNotEqualTo(orderDto2);
  }
}
