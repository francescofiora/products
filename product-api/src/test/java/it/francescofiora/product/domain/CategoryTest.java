package it.francescofiora.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.util.TestUtils;
import org.junit.jupiter.api.Test;

public class CategoryTest {

  @Test
  public void equalsVerifier() throws Exception {
    TestUtils.equalsVerifier(Category.class);
    Category category1 = new Category();
    category1.setId(1L);
    Category category2 = new Category();
    category2.setId(category1.getId());
    assertThat(category1).isEqualTo(category2);
    category2.setId(2L);
    assertThat(category1).isNotEqualTo(category2);
    category1.setId(null);
    assertThat(category1).isNotEqualTo(category2);
  }
}
