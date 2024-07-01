package it.francescofiora.product.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.domain.Category;
import it.francescofiora.product.api.domain.DomainIdentifier;
import it.francescofiora.product.api.domain.Order;
import it.francescofiora.product.api.domain.Product;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.RefCategoryDto;
import it.francescofiora.product.api.service.dto.RefCustomerDto;
import it.francescofiora.product.api.service.dto.RefProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
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
    order.setId(id);
    order.setCode("CODE");
    order.setCustomerId(2L);
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
    product.setId(id);
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
    order.setCustomer(createRefCustomerDto(1L));
    order.setPlacedDate(Instant.now());
    order.getItems().add(createNewOrderItemDto());
    return order;
  }

  /**
   * Create an simple example of NewOrderDto.
   *
   * @return NewOrderDto
   */
  public static NewOrderDto createNewSimpleOrderDto() {
    var order = new NewOrderDto();
    order.setCode("New CODE");
    order.setCustomer(createRefCustomerDto(1L));
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
   * Create RefCustomerDto.
   *
   * @param id the id of the Customer
   * @return RefCustomerDto
   */
  public static RefCustomerDto createRefCustomerDto(Long id) {
    var customer = new RefCustomerDto();
    customer.setId(id);
    return customer;
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
    order.setCustomer(createRefCustomerDto(3L));
    order.setPlacedDate(Instant.now());
    return order;
  }

  /**
   * Create new DomainIdentifier.
   *
   * @param clazz the DomainIdentifier class.
   * @param id the id
   * @return a new DomainIdentifier Object
   * @throws Exception if error occurs
   */
  public static <T> Object createNewDomain(Class<T> clazz, Long id) throws Exception {
    var domainObj = (DomainIdentifier) clazz.getConstructor().newInstance();
    domainObj.setId(id);
    return domainObj;
  }

  /**
   * Assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1).isEqualTo(obj2);
    assertThat(obj1).hasSameHashCodeAs(obj2.hashCode());
    assertThat(obj1).hasToString(obj2.toString());
  }

  /**
   * Assert that obj1 is not equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1).isNotEqualTo(obj2);
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }
}
