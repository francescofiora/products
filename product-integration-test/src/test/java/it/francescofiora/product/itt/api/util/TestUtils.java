package it.francescofiora.product.itt.api.util;

import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import it.francescofiora.product.api.service.dto.RefProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.service.dto.enumeration.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility for testing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  /**
   * Create an example of NewCategoryDto.
   *
   * @return NewCategoryDto
   */
  public static NewCategoryDto createNewCategoryDto() {
    var category = new NewCategoryDto();
    category.setName("New Name");
    category.setDescription("New Description");
    return category;
  }

  /**
   * Create an example of CategoryDto.
   *
   * @param id the id of the category
   * @return CategoryDto
   */
  public static CategoryDto createCategoryDto(Long id) {
    var category = new CategoryDto();
    category.setId(id);
    category.setName("Name");
    category.setDescription("Description");
    return category;
  }

  /**
   * Create an example of NewOrderItemDto.
   *
   * @param id the id of the product
   * @return NewOrderItemDto
   */
  public static NewOrderItemDto createNewOrderItemDto(Long id) {
    var item = new NewOrderItemDto();
    var product = new RefProductDto();
    product.setId(id);
    item.setProduct(product);
    item.setQuantity(10);
    return item;
  }

  /**
   * Create an example of NewOrderDto.
   *
   * @param productId the id of the product
   * @return NewOrderDto
   */
  public static NewOrderDto createNewOrderDto(Long productId) {
    var order = new NewOrderDto();
    order.setCode("New CODE");
    order.setCustomer("New Customer");
    order.setPlacedDate(Instant.now());
    order.getItems().add(createNewOrderItemDto(productId));
    return order;
  }

  /**
   * Create an example of NewOrderDto.
   *
   * @return NewOrderDto
   */
  public static NewOrderDto createNewSimpleOrderDto() {
    var order = new NewOrderDto();
    order.setCode("New CODE");
    order.setCustomer("New Customer");
    order.setPlacedDate(Instant.now());
    return order;
  }

  /**
   * Create an example of NewProductDto.
   *
   * @param categoryId the id of the category
   * @return NewProductDto
   */
  public static NewProductDto createNewProductDto(Long categoryId) {
    var product = new NewProductDto();
    product.setDescription("New Description");
    product.setImage("New Image");
    product.setImageContentType("JPG");
    product.setName("New Name");
    product.setPrice(BigDecimal.TEN);
    product.setSize(Size.L);
    product.setCategory(TestUtils.createRefCategoryDto(categoryId));
    return product;
  }

  /**
   * Create a second example of NewProductDto.
   *
   * @param categoryId the id of the category
   * @return NewProductDto
   */
  public static NewProductDto createNewProductDto2(Long categoryId) {
    var product = new NewProductDto();
    product.setDescription("New second Description");
    product.setImage("New second Image");
    product.setImageContentType("JPG");
    product.setName("New second Name");
    product.setPrice(BigDecimal.TEN);
    product.setSize(Size.L);
    product.setCategory(TestUtils.createRefCategoryDto(categoryId));
    return product;
  }

  /**
   * Create RefCategoryDto.
   *
   * @param id the id of the Category
   * @return RefCategoryDto
   */
  public static RefCategoryDto createRefCategoryDto(Long id) {
    var category = new RefCategoryDto();
    category.setId(id);
    return category;
  }

  /**
   * Create an example of UpdatebleProductDto.
   *
   * @param productId the id of the product
   * @param categoryId the id of the category
   * @return UpdatebleProductDto
   */
  public static UpdatebleProductDto createUpdatebleProductDto(Long productId, Long categoryId) {
    var product = new UpdatebleProductDto();
    product.setId(productId);
    product.setDescription("Description updated");
    product.setImage("Image updated");
    product.setImageContentType("GIF");
    product.setName("Name updated");
    product.setPrice(BigDecimal.valueOf(5L));
    product.setSize(Size.M);
    product.setCategory(TestUtils.createRefCategoryDto(categoryId));
    return product;
  }

  /**
   * Create an example of UpdatebleOrderDto.
   *
   * @param id the id
   * @return UpdatebleOrderDto
   */
  public static UpdatebleOrderDto createUpdatebleOrderDto(Long id) {
    var order = new UpdatebleOrderDto();
    order.setId(id);
    order.setCode("CODE updated");
    order.setCustomer("Customer updated");
    order.setPlacedDate(Instant.now());
    return order;
  }
}
