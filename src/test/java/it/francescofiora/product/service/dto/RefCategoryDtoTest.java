package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import it.francescofiora.product.TestUtil;

public class RefCategoryDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(RefCategoryDto.class);
    RefCategoryDto categoryDto1 = new RefCategoryDto();
    categoryDto1.setId(1L);
    RefCategoryDto categoryDto2 = new RefCategoryDto();
    assertThat(categoryDto1).isNotEqualTo(categoryDto2);
    categoryDto2.setId(categoryDto1.getId());
    assertThat(categoryDto1).isEqualTo(categoryDto2);
    categoryDto2.setId(2L);
    assertThat(categoryDto1).isNotEqualTo(categoryDto2);
    categoryDto1.setId(null);
    assertThat(categoryDto1).isNotEqualTo(categoryDto2);
  }  
}
