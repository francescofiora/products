package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.service.util.TestUtils;
import org.junit.jupiter.api.Test;

public class NewOrderItemDtoTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    NewOrderItemDto itemDto1 = TestUtils.createNewOrderItemDto();
    NewOrderItemDto itemDto2 = new NewOrderItemDto();
    assertThat(itemDto1).isNotEqualTo(itemDto2);

    itemDto2 = TestUtils.createNewOrderItemDto();
    TestUtils.checkEqualHashAndToString(itemDto1, itemDto2);

    RefProductDto productDto = new RefProductDto();
    productDto.setId(2L);
    itemDto2.setProduct(productDto);
    TestUtils.checkNotEqualHashAndToString(itemDto1, itemDto2);
    itemDto1.setProduct(null);
    TestUtils.checkNotEqualHashAndToString(itemDto1, itemDto2);

    itemDto1 = TestUtils.createNewOrderItemDto();
    itemDto2 = TestUtils.createNewOrderItemDto();
    itemDto2.setQuantity(20);
    TestUtils.checkNotEqualHashAndToString(itemDto1, itemDto2);
    itemDto1.setQuantity(null);
    TestUtils.checkNotEqualHashAndToString(itemDto1, itemDto2);
  }
}
