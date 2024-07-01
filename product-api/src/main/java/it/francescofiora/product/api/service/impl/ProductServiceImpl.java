package it.francescofiora.product.api.service.impl;

import it.francescofiora.product.api.domain.Category;
import it.francescofiora.product.api.domain.Product;
import it.francescofiora.product.api.repository.CategoryRepository;
import it.francescofiora.product.api.repository.ProductRepository;
import it.francescofiora.product.api.service.CategoryService;
import it.francescofiora.product.api.service.ProductService;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.service.mapper.ProductMapper;
import it.francescofiora.product.api.web.errors.NotFoundAlertException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Product.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();
  private static final GenericPropertyMatcher PROPERTY_MATCHER_EXACT =
      GenericPropertyMatchers.exact();

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;

  @Override
  public ProductDto create(NewProductDto productDto) {
    log.debug("Request to create Product : {}", productDto);

    if (categoryRepository.findById(productDto.getCategory().getId()).isEmpty()) {
      var id = String.valueOf(productDto.getCategory().getId());
      throw new NotFoundAlertException(CategoryService.ENTITY_NAME, id, "Category Not Found");
    }

    var product = productMapper.toEntity(productDto);
    product = productRepository.save(product);
    return productMapper.toDto(product);
  }

  @Override
  public void update(UpdatebleProductDto productDto) {
    log.debug("Request to update Product : {}", productDto);
    var productOpt = productRepository.findById(productDto.getId());
    if (productOpt.isEmpty()) {
      var id = String.valueOf(productDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, PRODUCT_NOT_FOUND);
    }
    var product = productOpt.get();
    productMapper.updateEntityFromDto(productDto, product);
    productRepository.save(product);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductDto> findAll(String name, String description, Long categoryId,
      Pageable pageable) {
    log.debug("Request to get all Products");
    var product = new Product();
    product.setName(name);
    product.setDescription(description);
    if (categoryId != null) {
      product.setCategory(new Category());
      product.getCategory().setId(categoryId);
    }
    var exampleMatcher = ExampleMatcher.matchingAll()
        .withMatcher("name", PROPERTY_MATCHER_DEFAULT)
        .withMatcher("description", PROPERTY_MATCHER_DEFAULT)
        .withMatcher("categoryId", PROPERTY_MATCHER_EXACT);
    var example = Example.of(product, exampleMatcher);
    return productRepository.findAll(example, pageable).map(productMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ProductDto> findOne(Long id) {
    log.debug("Request to get Product : {}", id);
    return productRepository.findById(id).map(productMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Product : {}", id);
    productRepository.deleteById(id);
  }
}
