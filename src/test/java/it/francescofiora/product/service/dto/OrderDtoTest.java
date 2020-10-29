package it.francescofiora.product.service.dto;

import org.junit.jupiter.api.Test;
import it.francescofiora.product.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(OrderDto.class);
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
