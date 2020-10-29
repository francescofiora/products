package it.francescofiora.product.service.dto;

import org.junit.jupiter.api.Test;
import it.francescofiora.product.TestUtil;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(CategoryDto.class);
    CategoryDto categoryDto1 = new CategoryDto();
    categoryDto1.setId(1L);
    CategoryDto categoryDto2 = new CategoryDto();
    assertThat(categoryDto1).isNotEqualTo(categoryDto2);
    categoryDto2.setId(categoryDto1.getId());
    assertThat(categoryDto1).isEqualTo(categoryDto2);
    categoryDto2.setId(2L);
    assertThat(categoryDto1).isNotEqualTo(categoryDto2);
    categoryDto1.setId(null);
    assertThat(categoryDto1).isNotEqualTo(categoryDto2);
  }
}
