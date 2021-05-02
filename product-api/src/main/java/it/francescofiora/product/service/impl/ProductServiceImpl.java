package it.francescofiora.product.service.impl;

import it.francescofiora.product.domain.Product;
import it.francescofiora.product.repository.CategoryRepository;
import it.francescofiora.product.repository.ProductRepository;
import it.francescofiora.product.service.CategoryService;
import it.francescofiora.product.service.ProductService;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.ProductDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import it.francescofiora.product.service.mapper.ProductMapper;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

  private final ProductRepository productRepository;

  private final ProductMapper productMapper;

  private final CategoryRepository categoryRepository;

  /**
   * Constructor.
   * 
   * @param productRepository ProductRepository
   * @param productMapper ProductMapper
   * @param categoryRepository CategoryRepository
   */
  public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
      CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public ProductDto create(NewProductDto productDto) {
    log.debug("Request to create Product : {}", productDto);

    if (!categoryRepository.findById(productDto.getCategory().getId()).isPresent()) {
      String id = String.valueOf(productDto.getCategory().getId());
      throw new NotFoundAlertException(CategoryService.ENTITY_NAME, id, "Category Not Found");
    }

    Product product = productMapper.toEntity(productDto);
    product = productRepository.save(product);
    return productMapper.toDto(product);
  }

  @Override
  public void update(UpdatebleProductDto productDto) {
    log.debug("Request to update Product : {}", productDto);
    Optional<Product> productOpt = productRepository.findById(productDto.getId());
    if (!productOpt.isPresent()) {
      String id = String.valueOf(productDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, PRODUCT_NOT_FOUND);
    }
    Product product = productOpt.get();
    productMapper.updateEntityFromDto(productDto, product);
    productRepository.save(product);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductDto> findAll(Pageable pageable) {
    log.debug("Request to get all Products");
    return productRepository.findAll(pageable).map(productMapper::toDto);
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
