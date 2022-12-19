package it.francescofiora.product.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.product.api.domain.Category;
import it.francescofiora.product.api.repository.CategoryRepository;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.impl.CategoryServiceImpl;
import it.francescofiora.product.api.service.mapper.CategoryMapper;
import it.francescofiora.product.api.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class CategoryServiceTest {

  private static final Long ID = 1L;

  @Test
  void testCreate() {
    var category = new Category();
    var categoryMapper = mock(CategoryMapper.class);
    when(categoryMapper.toEntity(any(NewCategoryDto.class))).thenReturn(category);

    var categoryRepository = mock(CategoryRepository.class);
    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    var expected = new CategoryDto();
    expected.setId(ID);
    when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

    var categoryDto = new NewCategoryDto();
    var categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
    var actual = categoryService.create(categoryDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var categoryDto = new CategoryDto();
    var categoryService =
        new CategoryServiceImpl(mock(CategoryRepository.class), mock(CategoryMapper.class));
    assertThrows(NotFoundAlertException.class, () -> categoryService.update(categoryDto));
  }

  @Test
  void testUpdate() {
    var category = new Category();
    var categoryRepository = mock(CategoryRepository.class);
    when(categoryRepository.findById(eq(ID))).thenReturn(Optional.of(category));

    var categoryDto = new CategoryDto();
    categoryDto.setId(ID);
    var categoryService = new CategoryServiceImpl(categoryRepository, mock(CategoryMapper.class));
    categoryService.update(categoryDto);
  }

  @Test
  void testFindAll() {
    var category = new Category();
    var categoryRepository = mock(CategoryRepository.class);
    when(categoryRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Category>(List.of(category)));
    var expected = new CategoryDto();
    var categoryMapper = mock(CategoryMapper.class);
    when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
    var page = categoryService.findAll(pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var categoryService =
        new CategoryServiceImpl(mock(CategoryRepository.class), mock(CategoryMapper.class));
    var categoryOpt = categoryService.findOne(ID);
    assertThat(categoryOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var category = new Category();
    category.setId(ID);
    var categoryRepository = mock(CategoryRepository.class);
    when(categoryRepository.findById(eq(category.getId()))).thenReturn(Optional.of(category));
    var expected = new CategoryDto();
    var categoryMapper = mock(CategoryMapper.class);
    when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

    var categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
    var categoryOpt = categoryService.findOne(ID);
    assertThat(categoryOpt).isPresent();
    var actual = categoryOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var categoryRepository = mock(CategoryRepository.class);
    var categoryService = new CategoryServiceImpl(categoryRepository, mock(CategoryMapper.class));
    categoryService.delete(ID);
    verify(categoryRepository).deleteById(ID);
  }

}
