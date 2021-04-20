package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.service.util.TestUtils;
import org.junit.jupiter.api.Test;

public class UpdatebleOrderDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtils.equalsVerifier(UpdatebleOrderDto.class);
    UpdatebleOrderDto orderDto1 = new UpdatebleOrderDto();
    orderDto1.setId(1L);
    UpdatebleOrderDto orderDto2 = new UpdatebleOrderDto();
    assertThat(orderDto1).isNotEqualTo(orderDto2);
    orderDto2.setId(orderDto1.getId());
    assertThat(orderDto1).isEqualTo(orderDto2);
    orderDto2.setId(2L);
    assertThat(orderDto1).isNotEqualTo(orderDto2);
    orderDto1.setId(null);
    assertThat(orderDto1).isNotEqualTo(orderDto2);
  } 
}
