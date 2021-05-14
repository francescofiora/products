package it.francescofiora.product.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.domain.Order;
import it.francescofiora.product.domain.Product;
import it.francescofiora.product.domain.User;
import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.RefCategoryDto;
import it.francescofiora.product.service.dto.RefProductDto;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import it.francescofiora.product.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.service.dto.enumeration.Size;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility for testing.
 */
public interface TestUtils {

  /**
   * Create User.
   *
   * @param username the username
   * @param role the role
   * @return the User
   */
  static User createUser(String username, String password, String role) {
    User user = new User();
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
  static Category createCategory(Long id) {
    Category category = new Category();
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
  static Order createOrder(Long id) {
    Order order = new Order();
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
  static Order createPendingOrder(Long id) {
    Order order = createOrder(id);
    order.setStatus(OrderStatus.PENDING);
    return order;
  }

  /**
   * Create an example of Product.
   *
   * @param id the id
   * @return Product
   */
  static Product createProduct(Long id) {
    Product product = new Product();
    product.setName("Name");
    product.setDescription("Description");
    return product;
  }

  /**
   * Create an example of NewCategoryDto.
   *
   * @return NewCategoryDto
   */
  static NewCategoryDto createNewCategoryDto() {
    NewCategoryDto category = new NewCategoryDto();
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
  static CategoryDto createCategoryDto(Long id) {
    CategoryDto category = new CategoryDto();
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
  static NewOrderItemDto createNewOrderItemDto(Long id) {
    NewOrderItemDto item = new NewOrderItemDto();
    RefProductDto product = new RefProductDto();
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
  static NewOrderItemDto createNewOrderItemDto() {
    return createNewOrderItemDto(1L);
  }

  /**
   * Create an example of NewOrderDto.
   *
   * @return NewOrderDto
   */
  static NewOrderDto createNewOrderDto() {
    NewOrderDto order = new NewOrderDto();
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
  static NewOrderDto createNewSimpleOrderDto() {
    NewOrderDto order = new NewOrderDto();
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
  static NewProductDto createNewProductDto() {
    NewProductDto product = new NewProductDto();
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
  static RefCategoryDto createRefCategoryDto(Long id) {
    RefCategoryDto category = new RefCategoryDto();
    category.setId(id);
    return category;
  }

  /**
   * Create an example of UpdatebleProductDto.
   *
   * @param id the id
   * @return UpdatebleProductDto
   */
  static UpdatebleProductDto createUpdatebleProductDto(Long id) {
    UpdatebleProductDto product = new UpdatebleProductDto();
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
  static UpdatebleOrderDto createUpdatebleOrderDto(Long id) {
    UpdatebleOrderDto order = new UpdatebleOrderDto();
    order.setId(id);
    order.setCode("CODE updated");
    order.setCustomer("Customer updated");
    order.setPlacedDate(Instant.now());
    return order;
  }

  /**
   * Verifies the equals/hashcode contract on the domain object.
   */
  static <T> void equalsVerifier(Class<T> clazz) throws Exception {
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
