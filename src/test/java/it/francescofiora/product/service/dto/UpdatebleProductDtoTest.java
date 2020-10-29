package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import it.francescofiora.product.TestUtil;

public class UpdatebleProductDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProductDto.class);
    UpdatebleProductDto productDto1 = new UpdatebleProductDto();
    productDto1.setId(1L);
    UpdatebleProductDto productDto2 = new UpdatebleProductDto();
    assertThat(productDto1).isNotEqualTo(productDto2);
    productDto2.setId(productDto1.getId());
    assertThat(productDto1).isEqualTo(productDto2);
    productDto2.setId(2L);
    assertThat(productDto1).isNotEqualTo(productDto2);
    productDto1.setId(null);
    assertThat(productDto1).isNotEqualTo(productDto2);
  }
}
