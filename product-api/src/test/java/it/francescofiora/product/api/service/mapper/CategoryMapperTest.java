package it.francescofiora.product.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import org.junit.jupiter.api.Test;

class CategoryMapperTest {

  @Test
  void testEntityFromId() {
    var id = 1L;
    var categoryMapper = new CategoryMapperImpl();
    assertThat(categoryMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(categoryMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    var categoryMapper = new CategoryMapperImpl();
    assertThat(categoryMapper.toDto(null)).isNull();

    var categoryDto = (NewCategoryDto) null;
    assertThat(categoryMapper.toEntity(categoryDto)).isNull();

    var dto = (RefCategoryDto) null;
    assertThat(categoryMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> categoryMapper.updateEntityFromDto(null, null));
  }
}
