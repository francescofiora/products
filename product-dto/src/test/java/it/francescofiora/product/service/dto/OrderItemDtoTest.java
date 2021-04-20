package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.service.util.TestUtils;
import org.junit.jupiter.api.Test;

public class OrderItemDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtils.equalsVerifier(OrderItemDto.class);
    OrderItemDto orderItemDto1 = new OrderItemDto();
    orderItemDto1.setId(1L);
    OrderItemDto orderItemDto2 = new OrderItemDto();
    assertThat(orderItemDto1).isNotEqualTo(orderItemDto2);
    orderItemDto2.setId(orderItemDto1.getId());
    assertThat(orderItemDto1).isEqualTo(orderItemDto2);
    orderItemDto2.setId(2L);
    assertThat(orderItemDto1).isNotEqualTo(orderItemDto2);
    orderItemDto1.setId(null);
    assertThat(orderItemDto1).isNotEqualTo(orderItemDto2);
  } 
}
