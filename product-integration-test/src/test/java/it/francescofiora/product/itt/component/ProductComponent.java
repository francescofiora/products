package it.francescofiora.product.itt.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.francescofiora.product.client.CategoryApiService;
import it.francescofiora.product.client.ProductApiService;
import it.francescofiora.product.itt.context.CategoryContext;
import it.francescofiora.product.itt.context.ProductContext;
import it.francescofiora.product.itt.util.TestProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Product Component.
 */
@Component
@RequiredArgsConstructor
public class ProductComponent extends AbstractComponent {

  private final CategoryContext categoryContext;
  private final ProductContext productContext;
  private final ProductApiService productApiService;
  private final CategoryApiService categoryApiService;

  /**
   * Create NewProductDto.
   *
   * @param name the name of the product
   * @param description the description of the product
   * @param image the image of the product
   * @param imageType the imageType of the image of the product
   * @param price the price of the product
   * @param size the size of the product
   */
  public void createNewProductDto(String name, String description, String image, String imageType,
      String price, String size) {
    var resultCat = categoryApiService.createCategory(categoryContext.getNewCategoryDto());
    categoryContext.setCategoryId(validateResponseAndGetId(resultCat));
    productContext.setNewProductDto(TestProductUtils.createNewProductDto(name, description, image,
        imageType, price, size, categoryContext.getCategoryId()));
  }

  public void createProduct() {
    var result = productApiService.createProduct(productContext.getNewProductDto());
    productContext.setProductId(validateResponseAndGetId(result));
  }

  /**
   * Fetch Product.
   */
  public void fetchProduct() {
    var result = productApiService.getProductById(productContext.getProductId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    productContext.setProductDto(result.getBody());
  }

  /**
   * Update Product.
   *
   * @param name the name
   * @param description the description
   * @param image the image
   * @param imageType the imageType of the image
   * @param price the price
   * @param size the size
   */
  public void updateProduct(String name, String description, String image, String imageType,
      String price, String size) {
    var productId = productContext.getProductId();
    productContext.setUpdatebleProductDto(TestProductUtils.createUpdatebleProductDto(productId,
        name, description, image, imageType, price, size, categoryContext.getCategoryId()));
    var result = productApiService.updateProduct(
        productContext.getUpdatebleProductDto(), productId);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  public void deleteProduct() {
    var result = productApiService.deleteProductById(productContext.getProductId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Fetch all Products.
   */
  public void fetchAllProducts() {
    var result = productApiService.findProducts(null, null, null, Pageable.unpaged());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotEmpty();
    productContext.setProducts(result.getBody());
  }

  /**
   * Compare ProductDto with NewProductDto.
   */
  public void compareProductWithNewProduct() {
    var product = productContext.getProductDto();
    assertNotNull(product);
    var newProductDto = productContext.getNewProductDto();
    assertNotNull(newProductDto);
    assertThat(product.getName()).isEqualTo(newProductDto.getName());
    assertThat(product.getDescription()).isEqualTo(newProductDto.getDescription());
  }

  /**
   * Compare UpdatebleProduct into Products.
   */
  public void compareUpdatebleProductIntoProducts() {
    var productId = productContext.getProductId();
    var opt = productContext.getProducts().stream()
        .filter(prod -> productId.equals(prod.getId())).findAny();
    assertThat(opt).isNotEmpty();
    var upProductDto = productContext.getUpdatebleProductDto();
    assertThat(opt.get().getName()).isEqualTo(upProductDto.getName());
    assertThat(opt.get().getDescription()).isEqualTo(upProductDto.getDescription());
  }

  /**
   * Check Product not Exist.
   */
  public void checkProductNotExist() {
    var resultPr = productApiService.findProducts(null, null, null, Pageable.unpaged());
    assertThat(resultPr.getBody()).isNotNull();
    productContext.setProducts(resultPr.getBody());
    var productId = productContext.getProductId();
    var opt = productContext.getProducts().stream()
        .filter(prod -> productId.equals(prod.getId())).findAny();
    assertThat(opt).isEmpty();
  }
}
