package it.francescofiora.product.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.enumeration.Size;
import it.francescofiora.product.api.service.util.TestUtils;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class NewProductDtoTest {

  @Test
  void dtoEqualsVerifier() {
    var productDto1 = TestUtils.createNewProductDto();
    var productDto2 = new NewProductDto();
    assertThat(productDto1).isNotEqualTo(productDto2);

    productDto2 = TestUtils.createNewProductDto();
    TestUtils.checkEqualHashAndToString(productDto1, productDto2);

    productDto2.setName("NameDiff");
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
    productDto1.setName(null);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);

    productDto1 = TestUtils.createNewProductDto();
    productDto2 = TestUtils.createNewProductDto();
    productDto2.setDescription("DescriptionDiff");
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
    productDto1.setDescription(null);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);

    productDto1 = TestUtils.createNewProductDto();
    productDto2 = TestUtils.createNewProductDto();
    var categoryDto = new RefCategoryDto();
    categoryDto.setId(3L);
    productDto2.setCategory(categoryDto);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
    productDto1.setCategory(null);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);

    productDto1 = TestUtils.createNewProductDto();
    productDto2 = TestUtils.createNewProductDto();
    productDto2.setImage("ImageDiff");
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
    productDto1.setImage(null);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);

    productDto1 = TestUtils.createNewProductDto();
    productDto2 = TestUtils.createNewProductDto();
    productDto2.setImageContentType("GIF");
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
    productDto1.setImageContentType(null);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);

    productDto1 = TestUtils.createNewProductDto();
    productDto2 = TestUtils.createNewProductDto();
    productDto2.setPrice(BigDecimal.valueOf(20L));
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
    productDto1.setPrice(null);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);

    productDto1 = TestUtils.createNewProductDto();
    productDto2 = TestUtils.createNewProductDto();
    productDto2.setSize(Size.L);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
    productDto1.setSize(null);
    TestUtils.checkNotEqualHashAndToString(productDto1, productDto2);
  }
}
