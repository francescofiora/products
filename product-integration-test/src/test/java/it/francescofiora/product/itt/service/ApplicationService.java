package it.francescofiora.product.itt.service;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.client.ProductApiService;
import it.francescofiora.product.itt.component.CategoryComponent;
import it.francescofiora.product.itt.component.OrderComponent;
import it.francescofiora.product.itt.component.ProductComponent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Application Service.
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {

  private final ProductApiService productApiService;
  private final CategoryComponent categoryComponent;
  private final ProductComponent productComponent;
  private final OrderComponent orderComponent;

  private static ResponseEntity<String> resultString;

  /**
   * When GET the Application Health/Info.
   *
   * @param op Health or Info
   */
  public void whenGetApplication(final String op) {
    resultString = switch (op) {
      case "Health" -> productApiService.getHealth();
      case "Info" -> productApiService.getInfo();
      default -> throw new IllegalArgumentException("Unexpected value: " + op);
    };
    assertThat(resultString.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /**
   * Check then Status of last operation.
   *
   * @param expected expected result
   */
  public void thenResultContains(final String expected) {
    assertThat(resultString.getBody()).contains(expected);
  }


  /**
   * Given a new entity.
   *
   * @param entity the entity
   * @param rows the example
   */
  public void givenNew(final String entity, List<String> rows) {
    switch (entity) {
      case "Category" -> categoryComponent.createNewCategoryDto(rows.get(0), rows.get(1));
      case "Product" -> productComponent.createNewProductDto(rows.get(0), rows.get(1), rows.get(2),
          rows.get(3), rows.get(4), rows.get(5));
      case "Order" -> orderComponent.createNewOrderDto(rows.get(0), rows.get(1));
      case "OrderItem" -> orderComponent.createNewOrderItemDto(rows.get(0));
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * When create that entity.
   *
   * @param entity the entity
   */
  public void whenCreate(final String entity) {
    switch (entity) {
      case "Category" -> categoryComponent.createCategory();
      case "Product" -> productComponent.createProduct();
      case "Order" -> orderComponent.createOrder();
      case "OrderItem" -> orderComponent.createItem();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Then should be able to get that entity.
   *
   * @param entity the entity
   */
  public void thenGetCategory(final String entity) {
    switch (entity) {
      case "Category" -> categoryComponent.fetchCategory();
      case "Product" -> productComponent.fetchProduct();
      case "Order" -> orderComponent.fetchOrder();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Compare the object used from creation with the object from fetching GET.
   *
   * @param entity the entity
   * @param op1 POST or PUT
   * @param op2 GET or GET_ALL
   */
  public void thenCompareObject(final String entity, final String op1, final String op2) {
    if ("POST".equals(op1) && "GET".equals(op2)) {

      switch (entity) {
        case "Category" -> categoryComponent.compareCategoryWithNewCategory();
        case "Product" -> productComponent.compareProductWithNewProduct();
        case "Order" -> orderComponent.compareOrderWithNewOrder();
        default -> throw new IllegalArgumentException("Unexpected value: " + entity);
      }

    } else if ("PUT".equals(op1) && "GET_ALL".equals(op2)) {

      switch (entity) {
        case "Category" -> categoryComponent.compareUpdatebleCategoryIntoCategories();
        case "Product" -> productComponent.compareUpdatebleProductIntoProducts();
        case "Order" -> orderComponent.compareUpdatebleOrderIntoOrders();
        default -> throw new IllegalArgumentException("Unexpected value: " + entity);
      }

    } else {
      throw new IllegalArgumentException("Unexpected value: " + op1 + " and " + op2);
    }
  }

  public void checkItems(int size) {
    orderComponent.checkSizeItems(size);
  }

  /**
   * Update the entity.
   *
   * @param entity the entity
   * @param rows the example
   */
  public void whenUpdate(final String entity, final List<String> rows) {
    switch (entity) {
      case "Category" -> categoryComponent.updateCategory(rows.get(0), rows.get(1));
      case "Product" -> productComponent.updateProduct(
          rows.get(0), rows.get(1), rows.get(2), rows.get(3), rows.get(4), rows.get(5));
      case "Order" -> orderComponent.updateOrder(rows.get(0), rows.get(1));
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Delete entity.
   *
   * @param entity the entity
   */
  public void thenDelete(final String entity) {
    switch (entity) {
      case "Category" -> categoryComponent.deleteCategory();
      case "Product" -> productComponent.deleteProduct();
      case "Order" -> orderComponent.deleteOrder();
      case "OrderItem" -> orderComponent.deleteItem();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Fetch entity from GET.
   *
   * @param entity the entity
   */
  public void whenGetAll(final String entity) {
    switch (entity) {
      case "Categories" -> categoryComponent.fetchAllCategories();
      case "Products" -> productComponent.fetchAllProducts();
      case "Orders" -> orderComponent.fetchAllOrders();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Check the entity not exists.
   *
   * @param entity the entity
   */
  public void thenNotExist(final String entity) {
    switch (entity) {
      case "Category" -> categoryComponent.checkCategoryNotExist();
      case "Product" -> productComponent.checkProductNotExist();
      case "Order" -> orderComponent.checkOrderNotExist();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }
}
