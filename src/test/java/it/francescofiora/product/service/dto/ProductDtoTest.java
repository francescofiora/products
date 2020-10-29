package it.francescofiora.product.service.dto;

import org.junit.jupiter.api.Test;
import it.francescofiora.product.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProductDto.class);
    ProductDto productDto1 = new ProductDto();
    productDto1.setId(1L);
    ProductDto productDto2 = new ProductDto();
    assertThat(productDto1).isNotEqualTo(productDto2);
    productDto2.setId(productDto1.getId());
    assertThat(productDto1).isEqualTo(productDto2);
    productDto2.setId(2L);
    assertThat(productDto1).isNotEqualTo(productDto2);
    productDto1.setId(null);
    assertThat(productDto1).isNotEqualTo(productDto2);
  }
}
