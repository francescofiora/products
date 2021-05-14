package it.francescofiora.product.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.service.dto.NewCategoryDto;
import it.francescofiora.product.service.dto.RefCategoryDto;
import org.junit.jupiter.api.Test;

class CategoryMapperTest {

  private CategoryMapper categoryMapper = new CategoryMapperImpl();

  @Test
  void testEntityFromId() {
    Long id = 1L;
    assertThat(categoryMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(categoryMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    assertThat(categoryMapper.toDto(null)).isNull();

    NewCategoryDto categoryDto = null;
    assertThat(categoryMapper.toEntity(categoryDto)).isNull();

    RefCategoryDto dto = null;
    assertThat(categoryMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> categoryMapper.updateEntityFromDto(null, null));
  }
}
