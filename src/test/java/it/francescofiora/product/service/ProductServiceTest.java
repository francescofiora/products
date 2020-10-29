package it.francescofiora.product.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import it.francescofiora.product.domain.Product;
import it.francescofiora.product.repository.ProductRepository;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.ProductDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import it.francescofiora.product.service.impl.ProductServiceImpl;
import it.francescofiora.product.service.mapper.NewProductMapper;
import it.francescofiora.product.service.mapper.ProductMapper;
import it.francescofiora.product.service.mapper.UpdatebleProductMapper;
import it.francescofiora.product.web.errors.NotFoundAlertException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

  private static final Long ID = 1L;

  private ProductService productService;

  @MockBean
  private NewProductMapper newProductMapper;

  @MockBean
  private ProductMapper productMapper;

  @MockBean
  private UpdatebleProductMapper updatebleProductMapper;

  @MockBean
  private ProductRepository productRepository;

  @BeforeEach
  public void setUp() {
    productService = new ProductServiceImpl(productRepository, productMapper,
        updatebleProductMapper, newProductMapper);
  }

  @Test
  public void testCreate() throws Exception {
    Product product = new Product();
    Mockito.when(newProductMapper.toEntity(Mockito.any(NewProductDto.class))).thenReturn(product);
    Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
    ProductDto expected = new ProductDto();
    expected.setId(ID);
    Mockito.when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(expected);
    NewProductDto productDto = new NewProductDto();
    ProductDto actual = productService.create(productDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testUpdateNotFound() throws Exception {
    UpdatebleProductDto productDto = new UpdatebleProductDto();
    Assertions.assertThrows(NotFoundAlertException.class, () -> productService.update(productDto));
  }

  @Test
  public void testUpdate() throws Exception {
    Product product = new Product();
    Mockito.when(productRepository.findById(Mockito.eq(ID))).thenReturn(Optional.of(product));

    UpdatebleProductDto productDto = new UpdatebleProductDto();
    productDto.setId(ID);
    productService.update(productDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Product product = new Product();
    Mockito.when(productRepository.findAll(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<Product>(Collections.singletonList(product)));
    ProductDto expected = new ProductDto();
    Mockito.when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<ProductDto> page = productService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<ProductDto> productOpt = productService.findOne(ID);
    assertThat(productOpt).isNotPresent();
  }

  @Test
  public void testFindOne() throws Exception {
    Product product = new Product();
    product.setId(ID);
    Mockito.when(productRepository.findById(Mockito.eq(product.getId())))
        .thenReturn(Optional.of(product));
    ProductDto expected = new ProductDto();
    Mockito.when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(expected);

    Optional<ProductDto> productOpt = productService.findOne(ID);
    assertThat(productOpt).isPresent();
    ProductDto actual = productOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDelete() throws Exception {
    productService.delete(ID);
  }

}
