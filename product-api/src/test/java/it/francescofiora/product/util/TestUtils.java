package it.francescofiora.product.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.domain.Order;
import it.francescofiora.product.domain.Product;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtils {

  /**
   * Create an example of Category.
   *
   * @param id
   * @return Category
   */
  public static Category createCategory(Long id) {
    Category category = new Category();
    category.setId(id);
    category.setName("Name");
    category.setDescription("Description");
    return category;
  }

  /**
   * Create an example of Order.
   *
   * @param id
   * @return Order
   */
  public static Order createOrder(Long id) {
    Order order = new Order();
    order.setCode("CODE");
    order.setCustomer("Customer");
    return order;
  }

  /**
   * Create an example of Product.
   *
   * @param id
   * @return Product
   */
  public static Product createProduct(Long id) {
    Product product = new Product();
    product.setName("Name");
    product.setDescription("Description");
    return product;
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
