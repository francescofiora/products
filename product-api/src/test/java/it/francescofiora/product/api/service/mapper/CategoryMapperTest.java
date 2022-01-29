package it.francescofiora.product.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import org.junit.jupiter.api.Test;

class CategoryMapperTest {

  private CategoryMapper categoryMapper = new CategoryMapperImpl();

  @Test
  void testEntityFromId() {
    var id = 1L;
    assertThat(categoryMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(categoryMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    assertThat(categoryMapper.toDto(null)).isNull();

    var categoryDto = (NewCategoryDto) null;
    assertThat(categoryMapper.toEntity(categoryDto)).isNull();

    var dto = (RefCategoryDto) null;
    assertThat(categoryMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> categoryMapper.updateEntityFromDto(null, null));
  }
}
