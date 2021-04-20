package it.francescofiora.product.service.impl;

import it.francescofiora.product.domain.Product;
import it.francescofiora.product.repository.ProductRepository;
import it.francescofiora.product.service.ProductService;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.ProductDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import it.francescofiora.product.service.mapper.NewProductMapper;
import it.francescofiora.product.service.mapper.ProductMapper;
import it.francescofiora.product.service.mapper.UpdatebleProductMapper;
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

  public static final String ENTITY_NAME = "Product";
  
  private final ProductRepository productRepository;
  
  private final ProductMapper productMapper;

  private final UpdatebleProductMapper updatebleProductMapper;
  
  private final NewProductMapper newProductMapper;

  /**
   * constructor.
   * @param productRepository ProductRepository
   * @param productMapper ProductMapper
   * @param updatebleProductMapper UpdatebleProductMapper
   * @param newProductMapper NewProductMapper
   */
  public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
      UpdatebleProductMapper updatebleProductMapper, NewProductMapper newProductMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
    this.updatebleProductMapper = updatebleProductMapper;
    this.newProductMapper = newProductMapper;
  }

  @Override
  public ProductDto create(NewProductDto productDto) {
    log.debug("Request to create Product : {}", productDto);
    Product product = newProductMapper.toEntity(productDto);
    product = productRepository.save(product);
    return productMapper.toDto(product);
  }

  @Override
  public void update(UpdatebleProductDto productDto) {
    log.debug("Request to save Product : {}", productDto);
    Optional<Product> productOpt = productRepository.findById(productDto.getId());
    if (!productOpt.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME);
    }
    Product product = productOpt.get();
    updatebleProductMapper.updateEntityFromDto(productDto, product);
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
