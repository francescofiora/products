package it.francescofiora.product.client.impl;

import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.client.ClientInfo;
import it.francescofiora.product.client.ProductClientService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Product Client Service Impl.
 */
@Component
public class ProductClientServiceImpl extends AbtractClient implements ProductClientService {

  private static final String PRODUCTS_URI = "/product/api/v1/products";
  private static final String PRODUCTS_ID_URI = "/product/api/v1/products/{id}";

  public ProductClientServiceImpl(ClientInfo clientInfo) {
    super(clientInfo);
  }

  @Override
  public Mono<ResponseEntity<Void>> create(NewProductDto productDto) {
    return create(PRODUCTS_URI, productDto, NewProductDto.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> update(UpdatebleProductDto productDto) {
    return update(PRODUCTS_ID_URI, productDto, UpdatebleProductDto.class);
  }

  @Override
  public Flux<ProductDto> findAll(Pageable pageable) {
    return findAll(pageable, PRODUCTS_URI, ProductDto.class);
  }

  @Override
  public Mono<ResponseEntity<ProductDto>> findOne(Long id) {
    return findOne(PRODUCTS_ID_URI, id, ProductDto.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> delete(Long id) {
    return delete(PRODUCTS_ID_URI, id);
  }
}
