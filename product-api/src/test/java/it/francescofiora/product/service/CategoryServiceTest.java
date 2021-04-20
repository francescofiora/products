package it.francescofiora.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.repository.CategoryRepository;
import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;
import it.francescofiora.product.service.impl.CategoryServiceImpl;
import it.francescofiora.product.service.mapper.CategoryMapper;
import it.francescofiora.product.service.mapper.NewCategoryMapper;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

  private static final Long ID = 1L;

  private CategoryService categoryService;

  @MockBean
  private NewCategoryMapper newCategoryMapper;

  @MockBean
  private CategoryMapper categoryMapper;

  @MockBean
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setUp() {
    categoryService =
        new CategoryServiceImpl(categoryRepository, categoryMapper, newCategoryMapper);
  }

  @Test
  public void testCreate() throws Exception {
    Category category = new Category();
    Mockito.when(newCategoryMapper.toEntity(Mockito.any(NewCategoryDto.class)))
        .thenReturn(category);

    Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

    CategoryDto expected = new CategoryDto();
    expected.setId(ID);
    Mockito.when(categoryMapper.toDto(Mockito.any(Category.class))).thenReturn(expected);

    NewCategoryDto categoryDto = new NewCategoryDto();
    CategoryDto actual = categoryService.create(categoryDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testUpdateNotFound() throws Exception {
    CategoryDto categoryDto = new CategoryDto();
    Assertions.assertThrows(NotFoundAlertException.class,
        () -> categoryService.update(categoryDto));
  }

  @Test
  public void testUpdate() throws Exception {
    Category category = new Category();
    Mockito.when(categoryRepository.findById(Mockito.eq(ID))).thenReturn(Optional.of(category));

    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setId(ID);
    categoryService.update(categoryDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Category category = new Category();
    Mockito.when(categoryRepository.findAll(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<Category>(Collections.singletonList(category)));
    CategoryDto expected = new CategoryDto();
    Mockito.when(categoryMapper.toDto(Mockito.any(Category.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<CategoryDto> page = categoryService.findAll(pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<CategoryDto> categoryOpt = categoryService.findOne(ID);
    assertThat(categoryOpt).isNotPresent();
  }

  @Test
  public void testFindOne() throws Exception {
    Category category = new Category();
    category.setId(ID);
    Mockito.when(categoryRepository.findById(Mockito.eq(category.getId())))
        .thenReturn(Optional.of(category));
    CategoryDto expected = new CategoryDto();
    Mockito.when(categoryMapper.toDto(Mockito.any(Category.class))).thenReturn(expected);

    Optional<CategoryDto> categoryOpt = categoryService.findOne(ID);
    assertThat(categoryOpt).isPresent();
    CategoryDto actual = categoryOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDelete() throws Exception {
    categoryService.delete(ID);
  }
  
}
