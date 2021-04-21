package it.francescofiora.product.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.service.dto.NewCategoryDto;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.RefCategoryDto;
import it.francescofiora.product.service.dto.RefProductDto;
import it.francescofiora.product.service.dto.enumeration.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtils {

  private static final Instant NOW =  Instant.now();
  
  /**
   * Create an example of NewCategoryDto.
   *
   * @return NewCategoryDto
   */
  public static NewCategoryDto createNewCategoryDto() {
    NewCategoryDto categoryDto = new NewCategoryDto();
    categoryDto.setName("Name");
    categoryDto.setDescription("Description");
    return categoryDto;
  }

  /**
   * Create an example of NewOrderDto.
   *
   * @return NewOrderDto
   */
  public static NewOrderDto createNewOrderDto() {
    NewOrderDto orderDto = new NewOrderDto();
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
  public static NewOrderItemDto createNewOrderItemDto() {
    NewOrderItemDto orderItemDto = new NewOrderItemDto();
    RefProductDto productDto = new RefProductDto();
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
  public static NewProductDto createNewProductDto() {
    NewProductDto productDto = new NewProductDto();
    RefCategoryDto categoryDto = new RefCategoryDto();
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
  public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
    T domainObject1 = clazz.getConstructor().newInstance();
    assertThat(domainObject1.toString()).isNotNull();
    assertThat(domainObject1).isEqualTo(domainObject1);
    assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());
    // Test with an instance of another class
    Object testOtherObject = new Object();
    assertThat(domainObject1).isNotEqualTo(testOtherObject);
    assertThat(domainObject1).isNotEqualTo(null);
    // Test with an instance of the same class
    T domainObject2 = clazz.getConstructor().newInstance();
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
  public static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
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
  public static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isFalse();
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }

  private TestUtils() {}

}
