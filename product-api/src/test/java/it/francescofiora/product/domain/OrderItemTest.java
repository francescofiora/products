package it.francescofiora.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.util.TestUtils;
import org.junit.jupiter.api.Test;

public class OrderItemTest {

  @Test
  public void equalsVerifier() throws Exception {
    TestUtils.equalsVerifier(OrderItem.class);
    OrderItem orderItem1 = new OrderItem();
    orderItem1.setId(1L);
    OrderItem orderItem2 = new OrderItem();
    orderItem2.setId(orderItem1.getId());
    assertThat(orderItem1).isEqualTo(orderItem2);
    orderItem2.setId(2L);
    assertThat(orderItem1).isNotEqualTo(orderItem2);
    orderItem1.setId(null);
    assertThat(orderItem1).isNotEqualTo(orderItem2);
  }
}
