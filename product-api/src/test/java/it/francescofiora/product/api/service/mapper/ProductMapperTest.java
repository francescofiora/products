package it.francescofiora.product.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import it.francescofiora.product.api.service.dto.RefProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class ProductMapperTest {

  @Autowired
  private ProductMapper productMapper;

  @Test
  void testEntityFromId() {
    var id = 1L;
    assertThat(productMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(productMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
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

  @TestConfiguration
  static class TestContextConfiguration {

    @Bean
    ProductMapper productMapper() {
      return new ProductMapperImpl();
    }

    @Bean
    CategoryMapper categoryMapper() {
      return new CategoryMapperImpl();
    }
  }
}
