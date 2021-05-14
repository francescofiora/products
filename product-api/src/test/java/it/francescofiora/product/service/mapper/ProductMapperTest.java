package it.francescofiora.product.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.RefProductDto;
import org.junit.jupiter.api.Test;

class ProductMapperTest {

  private ProductMapper productMapper = new ProductMapperImpl();

  @Test
  void testEntityFromId() {
    Long id = 1L;
    assertThat(productMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(productMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    assertThat(productMapper.toDto(null)).isNull();

    NewProductDto productDto = null;
    assertThat(productMapper.toEntity(productDto)).isNull();

    RefProductDto dto = null;
    assertThat(productMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> productMapper.updateEntityFromDto(null, null));
  }
}
