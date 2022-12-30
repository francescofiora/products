package it.francescofiora.product.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import it.francescofiora.product.api.service.dto.RefProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ProductMapperTest {

  @Test
  void testEntityFromId() {
    var id = 1L;
    var productMapper = createProductMapper();
    assertThat(productMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(productMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    var productMapper = createProductMapper();
    assertThat(productMapper.toDto(null)).isNull();

    var productDto = (NewProductDto) null;
    assertThat(productMapper.toEntity(productDto)).isNull();

    var dto = (RefProductDto) null;
    assertThat(productMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> productMapper.updateEntityFromDto(null, null));

    productDto = new NewProductDto();
    assertThat(productMapper.toEntity(productDto).getCategory()).isNull();

    productDto.setCategory(new RefCategoryDto());
    assertThat(productMapper.toEntity(productDto).getCategory()).isNull();
  }

  private ProductMapper createProductMapper() {
    var productMapper = new ProductMapperImpl();
    ReflectionTestUtils.setField(productMapper, "categoryMapper", new CategoryMapperImpl());
    return productMapper;
  }
}
