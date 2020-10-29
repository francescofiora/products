package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import it.francescofiora.product.TestUtil;

public class RefProductDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(RefProductDto.class);
    RefProductDto productDto1 = new RefProductDto();
    productDto1.setId(1L);
    RefProductDto productDto2 = new RefProductDto();
    assertThat(productDto1).isNotEqualTo(productDto2);
    productDto2.setId(productDto1.getId());
    assertThat(productDto1).isEqualTo(productDto2);
    productDto2.setId(2L);
    assertThat(productDto1).isNotEqualTo(productDto2);
    productDto1.setId(null);
    assertThat(productDto1).isNotEqualTo(productDto2);
  }
}
