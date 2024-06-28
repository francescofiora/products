package it.francescofiora.product.itt.service;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.client.ProductApiService;
import it.francescofiora.product.company.client.CompanyApiService;
import it.francescofiora.product.itt.component.CategoryComponent;
import it.francescofiora.product.itt.component.CompanyComponent;
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

  private static final String CATEGORY = "Category";
  private static final String PRODUCT = "Product";
  private static final String COMPANY = "Company";
  private static final String ORDER = "Order";
  private static final String ORDER_ITEM = "OrderItem";
  private static final String ADDRESS = "Address";

  private final CompanyApiService companyApiService;
  private final ProductApiService productApiService;

  private final CompanyComponent companyComponent;

  private final CategoryComponent categoryComponent;
  private final ProductComponent productComponent;
  private final OrderComponent orderComponent;

  private static ResponseEntity<String> resultString;

  /**
   * When GET the Application Health/Info.
   *
   * @param op Health or Info
   */
  public void whenGetApplication(final String application, final String op) {
    resultString = switch (application + "-" + op) {
      case "Product-Health" -> productApiService.getHealth();
      case "Product-Info" -> productApiService.getInfo();
      case "Company-Health" -> companyApiService.getHealth();
      case "Company-Info" -> companyApiService.getInfo();
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
      case CATEGORY -> categoryComponent.createNewCategoryDto(rows.get(0), rows.get(1));
      case PRODUCT -> productComponent.createNewProductDto(rows.get(0), rows.get(1), rows.get(2),
          rows.get(3), rows.get(4), rows.get(5));
      case ORDER -> orderComponent.createNewOrderDto(rows.get(0), rows.get(1));
      case ORDER_ITEM -> orderComponent.createNewOrderItemDto(rows.get(0));
      case COMPANY -> companyComponent.createNewCompanyDto(rows.get(0), rows.get(1), rows.get(2),
          rows.get(3), rows.get(4), rows.get(5), rows.get(6), rows.get(7), rows.get(8),
          rows.get(9), rows.get(10));
      case ADDRESS -> companyComponent.createNewAddressDto(rows.get(0), rows.get(1), rows.get(2),
          rows.get(3), rows.get(4), rows.get(5), rows.get(6), rows.get(7));
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
      case CATEGORY -> categoryComponent.createCategory();
      case PRODUCT -> productComponent.createProduct();
      case ORDER -> orderComponent.createOrder();
      case ORDER_ITEM -> orderComponent.createItem();
      case COMPANY -> companyComponent.createCompany();
      case ADDRESS -> companyComponent.createAddress();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Then should be able to get that entity.
   *
   * @param entity the entity
   */
  public void thenGetEntity(final String entity) {
    switch (entity) {
      case CATEGORY -> categoryComponent.fetchCategory();
      case PRODUCT -> productComponent.fetchProduct();
      case ORDER -> orderComponent.fetchOrder();
      case COMPANY -> companyComponent.fetchCompany();
      case ADDRESS -> companyComponent.fetchAddress();
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
        case CATEGORY -> categoryComponent.compareCategoryWithNewCategory();
        case PRODUCT -> productComponent.compareProductWithNewProduct();
        case ORDER -> orderComponent.compareOrderWithNewOrder();
        case COMPANY -> companyComponent.compareCompanyWithNewCompany();
        case ADDRESS -> companyComponent.compareAddressWithNewAddress();
        default -> throw new IllegalArgumentException("Unexpected value: " + entity);
      }

    } else if ("PUT".equals(op1) && "GET_ALL".equals(op2)) {

      switch (entity) {
        case CATEGORY -> categoryComponent.compareUpdatebleCategoryIntoCategories();
        case PRODUCT -> productComponent.compareUpdatebleProductIntoProducts();
        case ORDER -> orderComponent.compareUpdatebleOrderIntoOrders();
        case COMPANY -> companyComponent.compareUpdatebleCompanyIntoCompanies();
        case ADDRESS -> companyComponent.compareUpdatebleAddressIntoAddresses();
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
      case CATEGORY -> categoryComponent.updateCategory(rows.get(0), rows.get(1));
      case PRODUCT -> productComponent.updateProduct(
          rows.get(0), rows.get(1), rows.get(2), rows.get(3), rows.get(4), rows.get(5));
      case ORDER -> orderComponent.updateOrder(rows.get(0), rows.get(1));
      case COMPANY -> companyComponent.updateCompany(rows.get(0), rows.get(1), rows.get(2));
      case ADDRESS -> companyComponent.updateAddress(rows.get(0), rows.get(1), rows.get(2),
          rows.get(3), rows.get(4), rows.get(5), rows.get(6), rows.get(7));
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
      case CATEGORY -> categoryComponent.deleteCategory();
      case PRODUCT -> productComponent.deleteProduct();
      case ORDER -> orderComponent.deleteOrder();
      case ORDER_ITEM -> orderComponent.deleteItem();
      case COMPANY -> companyComponent.deleteCompany();
      case ADDRESS -> companyComponent.deleteAddress();
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
      case "Companies" -> companyComponent.fetchAllCompanies();
      case "Addresses" -> companyComponent.fetchAllAddresses();
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
      case CATEGORY -> categoryComponent.checkCategoryNotExist();
      case PRODUCT -> productComponent.checkProductNotExist();
      case ORDER -> orderComponent.checkOrderNotExist();
      case COMPANY -> companyComponent.checkCompanyNotExist();
      case ADDRESS -> companyComponent.checkAddressNotExist();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }
}
