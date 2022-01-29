package it.francescofiora.product.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.francescofiora.product.api.domain.Category;
import it.francescofiora.product.api.domain.Product;
import it.francescofiora.product.api.repository.CategoryRepository;
import it.francescofiora.product.api.repository.ProductRepository;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.service.impl.ProductServiceImpl;
import it.francescofiora.product.api.service.mapper.ProductMapper;
import it.francescofiora.product.api.util.TestUtils;
import it.francescofiora.product.api.web.errors.NotFoundAlertException;
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
class ProductServiceTest {

  private static final Long ID = 1L;

  private ProductService productService;

  @MockBean
  private ProductMapper productMapper;

  @MockBean
  private ProductRepository productRepository;

  @MockBean
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setUp() {
    productService = new ProductServiceImpl(productRepository, productMapper, categoryRepository);
  }

  @Test
  void testCreate() throws Exception {
    var product = new Product();
    when(productMapper.toEntity(any(NewProductDto.class))).thenReturn(product);
    when(productRepository.save(any(Product.class))).thenReturn(product);
    var expected = new ProductDto();
    expected.setId(ID);
    when(productMapper.toDto(any(Product.class))).thenReturn(expected);
    var productDto = TestUtils.createNewProductDto();
    when(categoryRepository.findById(eq(productDto.getCategory().getId())))
        .thenReturn(Optional.of(new Category()));

    var actual = productService.create(productDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() throws Exception {
    var productDto = new UpdatebleProductDto();
    assertThrows(NotFoundAlertException.class, () -> productService.update(productDto));
  }

  @Test
  void testUpdate() throws Exception {
    var product = new Product();
    when(productRepository.findById(eq(ID))).thenReturn(Optional.of(product));

    var productDto = new UpdatebleProductDto();
    productDto.setId(ID);
    productService.update(productDto);
  }

  @Test
  void testFindAll() throws Exception {
    var product = new Product();
    when(productRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Product>(List.of(product)));
    var expected = new ProductDto();
    when(productMapper.toDto(any(Product.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = productService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var productOpt = productService.findOne(ID);
    assertThat(productOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var product = new Product();
    product.setId(ID);
    when(productRepository.findById(eq(product.getId()))).thenReturn(Optional.of(product));
    var expected = new ProductDto();
    when(productMapper.toDto(any(Product.class))).thenReturn(expected);

    var productOpt = productService.findOne(ID);
    assertThat(productOpt).isPresent();
    var actual = productOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    productService.delete(ID);
  }

}
