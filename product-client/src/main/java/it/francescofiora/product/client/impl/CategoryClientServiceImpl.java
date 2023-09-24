package it.francescofiora.product.client.impl;

import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.client.CategoryClientService;
import it.francescofiora.product.client.ClientInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Category Client Service Impl.
 */
@Component
public class CategoryClientServiceImpl extends AbtractClient implements CategoryClientService {

  private static final String CATEGORIES_URI = "/api/v1/categories";
  private static final String CATEGORIES_ID_URI = "/api/v1/categories/{id}";

  /**
   * Constructor.
   *
   * @param clientInfo the ClientInfo
   */
  public CategoryClientServiceImpl(ClientInfo clientInfo) {
    super(clientInfo);
  }

  @Override
  public Mono<ResponseEntity<Void>> create(NewCategoryDto categoryDto) {
    return create(CATEGORIES_URI, categoryDto, NewCategoryDto.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> update(CategoryDto categoryDto) {
    return update(CATEGORIES_ID_URI, categoryDto, CategoryDto.class);
  }

  @Override
  public Flux<CategoryDto> findAll(Pageable pageable) {
    return findAll(pageable, CATEGORIES_URI, CategoryDto.class);
  }

  @Override
  public Mono<ResponseEntity<CategoryDto>> findOne(Long id) {
    return findOne(CATEGORIES_ID_URI, id, CategoryDto.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> delete(Long id) {
    return delete(CATEGORIES_ID_URI, id);
  }

}
