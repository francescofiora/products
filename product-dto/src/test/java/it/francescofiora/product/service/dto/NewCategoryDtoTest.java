package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.service.util.TestUtils;
import org.junit.jupiter.api.Test;

class NewCategoryDtoTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    var categoryDto1 = TestUtils.createNewCategoryDto();
    var categoryDto2 = new NewCategoryDto();
    assertThat(categoryDto1).isNotEqualTo(categoryDto2);

    categoryDto2 = TestUtils.createNewCategoryDto();
    TestUtils.checkEqualHashAndToString(categoryDto1, categoryDto2);

    categoryDto2.setName("NameDiff");
    TestUtils.checkNotEqualHashAndToString(categoryDto1, categoryDto2);
    categoryDto1.setName(null);
    TestUtils.checkNotEqualHashAndToString(categoryDto1, categoryDto2);

    categoryDto1 = TestUtils.createNewCategoryDto();
    categoryDto2 = TestUtils.createNewCategoryDto();
    categoryDto2.setDescription("DescriptionDiff");
    TestUtils.checkNotEqualHashAndToString(categoryDto1, categoryDto2);
    categoryDto1.setDescription(null);
    TestUtils.checkNotEqualHashAndToString(categoryDto1, categoryDto2);
  }
}
