package it.francescofiora.product.itt.util;

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
 * Utility for testing product Application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestProductUtils {

  /**
   * Create a new NewCategoryDto.
   *
   * @param name the name
   * @param description the description
   *
   * @return NewCategoryDto
   */
  public static NewCategoryDto createNewCategoryDto(String name, String description) {
    var category = new NewCategoryDto();
    category.setName(name);
    category.setDescription(description);
    return category;
  }

  /**
   * Create an example of CategoryDto.
   *
   * @param id the id of the category
   * @param name the Name
   * @param description the Description
   * @return CategoryDto
   */
  public static CategoryDto createCategoryDto(Long id, String name, String description) {
    var category = new CategoryDto();
    category.setId(id);
    category.setName(name);
    category.setDescription(description);
    return category;
  }

  /**
   * Create an example of NewOrderItemDto.
   *
   * @param id the id of the product
   * @param quantity the Quantity
   * @return NewOrderItemDto
   */
  public static NewOrderItemDto createNewOrderItemDto(Long id, String quantity) {
    var item = new NewOrderItemDto();
    var product = new RefProductDto();
    product.setId(id);
    item.setProduct(product);
    item.setQuantity(Integer.valueOf(quantity));
    return item;
  }

  /**
   * Create an example of NewOrderDto.
   *
   * @param code the Code
   * @param customer the Customer
   * @return NewOrderDto
   */
  public static NewOrderDto createNewOrderDto(String code, String customer) {
    var order = new NewOrderDto();
    order.setCode(code);
    order.setCustomer(customer);
    order.setPlacedDate(Instant.now());
    return order;
  }

  /**
   * Create an example of NewProductDto.
   *
   * @param name the Name
   * @param description the Description
   * @param image the Image
   * @param imageType the ImageContentType
   * @param price the Price
   * @param size the Size
   * @param categoryId the id of the category
   * @return NewProductDto
   */
  public static NewProductDto createNewProductDto(String name, String description, String image,
      String imageType, String price, String size, Long categoryId) {
    var product = new NewProductDto();
    product.setName(name);
    product.setDescription(description);
    product.setImage(image);
    product.setImageContentType(imageType);
    product.setPrice(BigDecimal.valueOf(Double.valueOf(price)));
    product.setSize(Size.valueOf(size));
    product.setCategory(TestProductUtils.createRefCategoryDto(categoryId));
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
   * @param name the Name
   * @param description the Description
   * @param image the Image
   * @param imageType the ImageContentType
   * @param price the Price
   * @param size the Size
   * @param categoryId the id of the category
   * @return UpdatebleProductDto
   */
  public static UpdatebleProductDto createUpdatebleProductDto(Long productId, String name,
      String description, String image, String imageType, String price, String size,
      Long categoryId) {
    var product = new UpdatebleProductDto();
    product.setId(productId);
    product.setName(name);
    product.setDescription(description);
    product.setImage(image);
    product.setImageContentType(imageType);
    product.setPrice(BigDecimal.valueOf(Double.valueOf(price)));
    product.setSize(Size.valueOf(size));
    product.setCategory(TestProductUtils.createRefCategoryDto(categoryId));
    return product;
  }

  /**
   * Create an example of UpdatebleOrderDto.
   *
   * @param id the id
   * @param code the Code
   * @param customer the Customer
   * @return UpdatebleOrderDto
   */
  public static UpdatebleOrderDto createUpdatebleOrderDto(Long id, String code, String customer) {
    var order = new UpdatebleOrderDto();
    order.setId(id);
    order.setCode(code);
    order.setCustomer(customer);
    order.setPlacedDate(Instant.now());
    return order;
  }
}
