package it.francescofiora.product.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ProductServiceTest {

  private static final Long ID = 1L;

  @Test
  void testCreate() {
    var product = new Product();
    var productMapper = mock(ProductMapper.class);
    when(productMapper.toEntity(any(NewProductDto.class))).thenReturn(product);

    var productRepository = mock(ProductRepository.class);
    when(productRepository.save(any(Product.class))).thenReturn(product);

    var expected = new ProductDto();
    expected.setId(ID);
    when(productMapper.toDto(any(Product.class))).thenReturn(expected);

    var productDto = TestUtils.createNewProductDto();
    var categoryRepository = mock(CategoryRepository.class);
    when(categoryRepository.findById(productDto.getCategory().getId()))
        .thenReturn(Optional.of(new Category()));

    var productService =
        new ProductServiceImpl(productRepository, productMapper, categoryRepository);
    var actual = productService.create(productDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var productDto = new UpdatebleProductDto();
    var productService = new ProductServiceImpl(mock(ProductRepository.class),
        mock(ProductMapper.class), mock(CategoryRepository.class));
    assertThrows(NotFoundAlertException.class, () -> productService.update(productDto));
  }

  @Test
  void testUpdate() {
    var product = new Product();
    var productRepository = mock(ProductRepository.class);
    when(productRepository.findById(ID)).thenReturn(Optional.of(product));

    var productDto = new UpdatebleProductDto();
    productDto.setId(ID);
    var productMapper = mock(ProductMapper.class);
    var productService =
        new ProductServiceImpl(productRepository, productMapper, mock(CategoryRepository.class));
    productService.update(productDto);
    verify(productMapper).updateEntityFromDto(productDto, product);
    verify(productRepository).save(product);
  }

  @Test
  void testFindAll() {
    var product = new Product();
    var productRepository = mock(ProductRepository.class);
    when(productRepository.findAll(ArgumentMatchers.any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(product)));

    var expected = new ProductDto();
    var productMapper = mock(ProductMapper.class);
    when(productMapper.toDto(any(Product.class))).thenReturn(expected);

    var pageable = PageRequest.of(1, 1);
    var productService =
        new ProductServiceImpl(productRepository, productMapper, mock(CategoryRepository.class));
    var page = productService.findAll(null, null, null, pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var productService = new ProductServiceImpl(mock(ProductRepository.class),
        mock(ProductMapper.class), mock(CategoryRepository.class));
    var productOpt = productService.findOne(ID);
    assertThat(productOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var product = new Product();
    product.setId(ID);
    var productRepository = mock(ProductRepository.class);
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    var expected = new ProductDto();
    var productMapper = mock(ProductMapper.class);
    when(productMapper.toDto(any(Product.class))).thenReturn(expected);

    var productService =
        new ProductServiceImpl(productRepository, productMapper, mock(CategoryRepository.class));
    var productOpt = productService.findOne(ID);
    assertThat(productOpt).isPresent();
    var actual = productOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var productRepository = mock(ProductRepository.class);
    var productService = new ProductServiceImpl(productRepository, mock(ProductMapper.class),
        mock(CategoryRepository.class));
    productService.delete(ID);
    verify(productRepository).deleteById(ID);
  }
}
