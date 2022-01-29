package it.francescofiora.product.api.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import it.francescofiora.product.api.service.dto.RefProductDto;
import it.francescofiora.product.api.service.dto.enumeration.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Utility for testing.
 */
public interface TestUtils {

  static final Instant NOW =  Instant.now();
  
  /**
   * Create an example of NewCategoryDto.
   *
   * @return NewCategoryDto
   */
  static NewCategoryDto createNewCategoryDto() {
    var categoryDto = new NewCategoryDto();
    categoryDto.setName("Name");
    categoryDto.setDescription("Description");
    return categoryDto;
  }

  /**
   * Create an example of NewOrderDto.
   *
   * @return NewOrderDto
   */
  static NewOrderDto createNewOrderDto() {
    var orderDto = new NewOrderDto();
    orderDto.setCode("Code");
    orderDto.setCustomer("Customer");
    orderDto.setPlacedDate(NOW);
    orderDto.getItems().add(createNewOrderItemDto());
    return orderDto;
  }

  /**
   * Create an example of NewOrderItemDto.
   *
   * @return NewOrderItemDto
   */
  static NewOrderItemDto createNewOrderItemDto() {
    var orderItemDto = new NewOrderItemDto();
    var productDto = new RefProductDto();
    productDto.setId(1L);
    orderItemDto.setProduct(productDto);
    orderItemDto.setQuantity(10);
    return orderItemDto;
  }

  /**
   * Create an example of NewProductDto.
   *
   * @return NewProductDto
   */
  static NewProductDto createNewProductDto() {
    var productDto = new NewProductDto();
    var categoryDto = new RefCategoryDto();
    categoryDto.setId(1L);
    productDto.setCategory(categoryDto);
    productDto.setDescription("Description");
    productDto.setImage("Image");
    productDto.setImageContentType("JPG");
    productDto.setName("Name");
    productDto.setPrice(BigDecimal.TEN);
    productDto.setSize(Size.M);
    return productDto;
  }

  /**
   * Verifies the equals/hashcode contract on the domain object.
   */
  static <T> void equalsVerifier(Class<T> clazz) throws Exception {
    var domainObject1 = clazz.getConstructor().newInstance();
    assertThat(domainObject1.toString()).isNotNull();
    assertThat(domainObject1).isEqualTo(domainObject1);
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());
    // Test with an instance of another class
    var testOtherObject = new Object();
    assertThat(domainObject1).isNotEqualTo(testOtherObject);
    assertThat(domainObject1).isNotEqualTo(null);
    // Test with an instance of the same class
    var domainObject2 = clazz.getConstructor().newInstance();
    assertThat(domainObject1).isNotEqualTo(domainObject2);
    // HashCodes are equals because the objects are not persisted yet
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject2.hashCode());
  }

  /**
   * assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isTrue();
    assertThat(obj1.hashCode()).isEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isEqualTo(obj2.toString());
  }

  /**
   * assert that obj1 is not equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isFalse();
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }
}
