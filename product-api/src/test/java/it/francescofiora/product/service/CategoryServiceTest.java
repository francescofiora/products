package it.francescofiora.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.repository.CategoryRepository;
import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;
import it.francescofiora.product.service.impl.CategoryServiceImpl;
import it.francescofiora.product.service.mapper.CategoryMapper;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

  private static final Long ID = 1L;

  private CategoryService categoryService;

  @MockBean
  private CategoryMapper categoryMapper;

  @MockBean
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setUp() {
    categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
  }

  @Test
  void testCreate() throws Exception {
    var category = new Category();
    when(categoryMapper.toEntity(any(NewCategoryDto.class))).thenReturn(category);

    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    var expected = new CategoryDto();
    expected.setId(ID);
    when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

    var categoryDto = new NewCategoryDto();
    var actual = categoryService.create(categoryDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() throws Exception {
    var categoryDto = new CategoryDto();
    assertThrows(NotFoundAlertException.class, () -> categoryService.update(categoryDto));
  }

  @Test
  void testUpdate() throws Exception {
    var category = new Category();
    when(categoryRepository.findById(eq(ID))).thenReturn(Optional.of(category));

    var categoryDto = new CategoryDto();
    categoryDto.setId(ID);
    categoryService.update(categoryDto);
  }

  @Test
  void testFindAll() throws Exception {
    var category = new Category();
    when(categoryRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Category>(List.of(category)));
    var expected = new CategoryDto();
    when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = categoryService.findAll(pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var categoryOpt = categoryService.findOne(ID);
    assertThat(categoryOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var category = new Category();
    category.setId(ID);
    when(categoryRepository.findById(eq(category.getId()))).thenReturn(Optional.of(category));
    var expected = new CategoryDto();
    when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

    var categoryOpt = categoryService.findOne(ID);
    assertThat(categoryOpt).isPresent();
    var actual = categoryOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    categoryService.delete(ID);
  }

}
