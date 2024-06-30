package it.francescofiora.product.api.web.api.impl;

import it.francescofiora.product.api.service.ProductService;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.web.api.ProductApi;
import it.francescofiora.product.api.web.errors.BadRequestAlertException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller implementation for managing Product.
 */
@RestController
public class ProductApiImpl extends AbstractApi implements ProductApi {

  private static final String ENTITY_NAME = "ProductDto";
  private static final String TAG = "product";

  private final ProductService productService;

  public ProductApiImpl(ProductService productService) {
    super(ENTITY_NAME);
    this.productService = productService;
  }

  @Override
  public ResponseEntity<Void> createProduct(NewProductDto productDto) {
    var result = productService.create(productDto);
    return postResponse("/api/v1/products/" + result.getId(), result.getId());
  }

  @Override
  public ResponseEntity<Void> updateProduct(UpdatebleProductDto productDto, Long id) {
    if (!id.equals(productDto.getId())) {
      throw new BadRequestAlertException("UpdatebleProductDto", String.valueOf(productDto.getId()),
          "Invalid id");
    }
    productService.update(productDto);
    return putResponse(id);
  }

  @Override
  public ResponseEntity<List<ProductDto>> findProducts(
      String name, String description, Long categoryId, Pageable pageable) {
    return getResponse(productService.findAll(name, description, categoryId, pageable));
  }

  @Override
  public ResponseEntity<ProductDto> getProductById(Long id) {
    return getResponse(productService.findOne(id), id);
  }

  @Override
  public ResponseEntity<Void> deleteProductById(Long id) {
    productService.delete(id);
    return deleteResponse(id);
  }
}
