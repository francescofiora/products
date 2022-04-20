package it.francescofiora.product.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.domain.Category;
import it.francescofiora.product.api.domain.Order;
import it.francescofiora.product.api.domain.Product;
import it.francescofiora.product.api.domain.User;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import it.francescofiora.product.api.service.dto.RefProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.api.service.dto.enumeration.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility for testing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  /**
   * Create User.
   *
   * @param username the username
   * @param role the role
   * @return the User
   */
  public static User createUser(String username, String password, String role) {
    var user = new User();
    user.setPassword(new BCryptPasswordEncoder().encode(password));
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);
    user.setEnabled(true);
    user.setRole(role);
    user.setUsername(username);
    return user;
  }


  /**
   * Create an example of Category.
   *
   * @param id the id
   * @return Category
   */
  public static Category createCategory(Long id) {
    var category = new Category();
    category.setId(id);
    category.setName("Name");
    category.setDescription("Description");
    return category;
  }

  /**
   * Create an example of Order.
   *
   * @param id the id
   * @return Order
   */
  public static Order createOrder(Long id) {
    var order = new Order();
    order.setCode("CODE");
    order.setCustomer("Customer");
    order.setStatus(OrderStatus.PENDING);
    return order;
  }

  /**
   * Create a pending Order.
   *
   * @param id the id
   * @return Order
   */
  public static Order createPendingOrder(Long id) {
    var order = createOrder(id);
    order.setStatus(OrderStatus.PENDING);
    return order;
  }

  /**
   * Create an example of Product.
   *
   * @param id the id
   * @return Product
   */
  public static Product createProduct(Long id) {
    var product = new Product();
    product.setName("Name");
    product.setDescription("Description");
    return product;
  }

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
   * @param id the id
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
   * Create an example of NewOrderItemDto.
   *
   * @return NewOrderItemDto
   */
  public static NewOrderItemDto createNewOrderItemDto() {
    return createNewOrderItemDto(1L);
  }

  /**
   * Create an example of NewOrderDto.
   *
   * @return NewOrderDto
   */
  public static NewOrderDto createNewOrderDto() {
    var order = new NewOrderDto();
    order.setCode("New CODE");
    order.setCustomer("New Customer");
    order.setPlacedDate(Instant.now());
    order.getItems().add(createNewOrderItemDto());
    return order;
  }

  /**
   * Create an simle example of NewOrderDto.
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
   * @return NewProductDto
   */
  public static NewProductDto createNewProductDto() {
    var product = new NewProductDto();
    product.setDescription("New Description");
    product.setImage("New Image");
    product.setImageContentType("JPG");
    product.setName("New Name");
    product.setPrice(BigDecimal.TEN);
    product.setSize(Size.L);
    product.setCategory(createRefCategoryDto(3L));
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
   * @param id the id
   * @return UpdatebleProductDto
   */
  public static UpdatebleProductDto createUpdatebleProductDto(Long id) {
    var product = new UpdatebleProductDto();
    product.setId(id);
    product.setDescription("Description updated");
    product.setImage("Image updated");
    product.setImageContentType("GIF");
    product.setName("Name updated");
    product.setPrice(BigDecimal.valueOf(5L));
    product.setSize(Size.M);
    product.setCategory(createRefCategoryDto(5L));
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

  /**
   * Verifies the equals/hashcode contract on the domain object.
   */
  public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
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
}
