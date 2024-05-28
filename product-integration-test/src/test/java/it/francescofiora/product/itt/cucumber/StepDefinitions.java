package it.francescofiora.product.itt.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.client.ProductApiService;
import it.francescofiora.product.itt.StartStopContainers;
import it.francescofiora.product.itt.api.AbstractTestContainer;
import it.francescofiora.product.itt.api.util.ContainerGenerator;
import it.francescofiora.product.itt.api.util.TestUtils;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Cucumber Step Definitions.
 */
@Slf4j
public class StepDefinitions extends AbstractTestContainer {

  private static final String DATASOURCE_URL =
      "jdbc:postgresql://product-postgresql:5432/db_product";

  private static StartStopContainers containers = new StartStopContainers();
  private static ContainerGenerator containerGenerator = new ContainerGenerator();

  private static NewCategoryDto newCategoryDto;
  private static NewProductDto newProductDto;
  private static NewOrderDto newOrderDto;
  private static NewOrderItemDto newOrderItemDto;
  private static CategoryDto categoryDto;
  private static UpdatebleProductDto upProductDto;
  private static UpdatebleOrderDto upOrderDto;
  private static ResponseEntity<Void> resultVoid;
  private static ResponseEntity<CategoryDto> resultCategory;
  private static ResponseEntity<ProductDto> resultProduct;
  private static ResponseEntity<OrderDto> resultOrder;
  private static List<CategoryDto> resultCategories;
  private static List<ProductDto> resultProducts;
  private static List<OrderDto> resultOrders;
  private static Long categoryId;
  private static Long productId;
  private static Long orderId;
  private static Long itemId;
  private static HttpStatusCode lastStatusCode;

  @Autowired
  private DiscoveryClient discoveryClient;

  @Autowired
  private ProductApiService productApiService;

  /**
   * Start all containers.
   *
   * @throws SQLException if errors occur
   */
  @BeforeAll
  public static void init() throws SQLException {
    postgreContainer = containerGenerator.createPostgreSqlContainer();
    containers.add(postgreContainer);

    try (var ds = createHikariDataSource(postgreContainer)) {
      executeQuery(ds, "CREATE SCHEMA IF NOT EXISTS STORE");
    }

    // @formatter:off
    eureka = containerGenerator.createContainer("francescofiora-product-eureka")
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withNetworkAliases(ContainerGenerator.PRODUCT_EUREKA)
        .withExposedPorts(8761);
    // @formatter:on
    containers.add(eureka);

    var eurekaHttp = "http://user:password@" + ContainerGenerator.PRODUCT_EUREKA + ":8761/eureka";

    // @formatter:off
    var product = containerGenerator.createContainer("francescofiora-product")
        .withEnv(Map.of(
            "DATASOURCE_URL", DATASOURCE_URL,
            "EUREKA_URI", eurekaHttp,
            "eureka.instance.prefer-ip-address", "true"))
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withNetworkAliases(ContainerGenerator.PRODUCT_API)
        .withExposedPorts(8081);
    // @formatter:on
    containers.add(product);
  }

  /**
   * Given the system up and running.
   */
  @Given("the system up and running")
  public void givenSystemUp() {
    assertTrue(containers.areRunning());
    while (discoveryClient.getInstances("PRODUCT-API").isEmpty()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * When GET the Application Health/Info.
   *
   * @param op Health or Info
   */
  @When("^GET the Application (\\w+)$")
  public void whenGetApplication(final String op) {
    var resultString = switch (op) {
      case "Health" -> productApiService.getHealth();
      case "Info" -> productApiService.getInfo();
      default -> throw new IllegalArgumentException("Unexpected value: " + op);
    };
    lastStatusCode = resultString.getStatusCode();
  }

  /**
   * Given a new entity.
   *
   * @param entity the entity
   * @param dataTable the example
   */
  @Given("^a new (\\w+)$")
  public void givenNew(final String entity, final DataTable dataTable) {
    var rows = dataTable.transpose().asList(String.class);
    switch (entity) {
      case "Category":
        newCategoryDto = TestUtils.createNewCategoryDto(rows.get(0), rows.get(1));
        break;

      case "Product":
        var resultCat = productApiService.createCategory(newCategoryDto);
        categoryId = validateResponseAndGetId(resultCat);
        newProductDto = TestUtils.createNewProductDto(rows.get(0), rows.get(1), rows.get(2),
            rows.get(3), rows.get(4), rows.get(5), categoryId);
        break;

      case "Order":
        newOrderDto = TestUtils.createNewOrderDto(rows.get(0), rows.get(1));
        newOrderDto.getItems().add(newOrderItemDto);
        break;

      case "OrderItem":
        var resultPr = productApiService.createProduct(newProductDto);
        productId = validateResponseAndGetId(resultPr);
        newOrderItemDto = TestUtils.createNewOrderItemDto(productId, rows.get(0));
        break;

      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * When create that entity.
   *
   * @param entity the entity
   */
  @When("^create that (\\w+)$")
  public void whenCreate(final String entity) {
    switch (entity) {
      case "Category":
        resultVoid = productApiService.createCategory(newCategoryDto);
        categoryId = validateResponseAndGetId(resultVoid);
        break;

      case "Product":
        resultVoid = productApiService.createProduct(newProductDto);
        productId = validateResponseAndGetId(resultVoid);
        break;

      case "Order":
        resultVoid = productApiService.createOrder(newOrderDto);
        orderId = validateResponseAndGetId(resultVoid);
        break;

      case "OrderItem":
        resultVoid = productApiService.addOrderItem(orderId, newOrderItemDto);
        itemId = validateResponseAndGetId(resultVoid);
        break;

      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Then should be able to get that entity.
   *
   * @param entity the entity
   */
  @Then("^should be able to get that (\\w+)$")
  public void thenGetCategory(final String entity) {
    switch (entity) {

      case "Category":
        resultCategory = productApiService.getCategoryById(categoryId);
        lastStatusCode = resultCategory.getStatusCode();
        break;

      case "Product":
        resultProduct = productApiService.getProductById(productId);
        lastStatusCode = resultProduct.getStatusCode();
        break;

      case "Order":
        resultOrder = productApiService.getOrderById(orderId);
        lastStatusCode = resultOrder.getStatusCode();
        break;

      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Check then Status of last operation.
   *
   * @param op GET, DELETE or PUT
   * @param statusCode the aspected statusCode
   */
  @Then("^the (\\w+) status should be (\\w+)$")
  public void thenStatusIs(final String op, final String statusCode) {
    var status = HttpStatus.valueOf(statusCode);
    switch (op) {
      case "GET" -> assertThat(lastStatusCode).isEqualTo(status);
      case "POST", "DELETE", "PUT" -> assertThat(resultVoid.getStatusCode()).isEqualTo(status);
      default -> throw new IllegalArgumentException("Unexpected value: " + op);
    }
  }

  /**
   * Compare the object used from creation with the object from fetching GET.
   *
   * @param entity the entity
   * @param op1 POST or PUT
   * @param op2 GET or GET_ALL
   * @throws JSONException if errors occur
   */
  @Then("^the (\\w+) from (\\w+) should the same as from (\\w+)$")
  public void thenCompareObject(final String entity, final String op1, final String op2)
      throws JSONException {
    var execution = false;
    switch (entity) {

      case "Category":
        if ("POST".equals(op1) && "GET".equals(op2)) {
          var category = resultCategory.getBody();
          assertNotNull(category);
          assertThat(category.getName()).isEqualTo(newCategoryDto.getName());
          assertThat(category.getDescription()).isEqualTo(newCategoryDto.getDescription());
          execution = true;
        }
        if ("PUT".equals(op1) && "GET_ALL".equals(op2)) {
          var opt = resultCategories.stream()
              .filter(cat -> categoryId.equals(cat.getId())).findAny();
          assertThat(opt).hasValue(categoryDto);
          execution = true;
        }
        break;

      case "Product":
        if ("POST".equals(op1) && "GET".equals(op2)) {
          var product = resultProduct.getBody();
          assertNotNull(product);
          assertThat(product.getName()).isEqualTo(newProductDto.getName());
          assertThat(product.getDescription()).isEqualTo(newProductDto.getDescription());
          execution = true;
        }
        if ("PUT".equals(op1) && "GET_ALL".equals(op2)) {
          var opt = resultProducts.stream()
              .filter(prod -> productId.equals(prod.getId())).findAny();
          assertThat(opt).isNotEmpty();
          assertThat(opt.get().getName()).isEqualTo(upProductDto.getName());
          assertThat(opt.get().getDescription()).isEqualTo(upProductDto.getDescription());
          execution = true;
        }
        break;

      case "Order":
        if ("POST".equals(op1) && "GET".equals(op2)) {
          var order = resultOrder.getBody();
          assertNotNull(order);
          assertThat(order.getCode()).isEqualTo(newOrderDto.getCode());
          assertThat(order.getCustomer()).isEqualTo(newOrderDto.getCustomer());
          execution = true;
        }
        if ("PUT".equals(op1) && "GET_ALL".equals(op2)) {
          var opt = resultOrders.stream().filter(order -> orderId.equals(order.getId())).findAny();
          assertThat(opt).isNotEmpty();
          assertThat(opt.get().getCode()).isEqualTo(upOrderDto.getCode());
          assertThat(opt.get().getCustomer()).isEqualTo(upOrderDto.getCustomer());
          execution = true;
        }
        break;

      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
    if (!execution) {
      throw new IllegalArgumentException("Unexpected value: " + op1 + " and " + op2);
    }
  }

  @Then("that Order should have {int} items")
  public void checkItems(int size) {
    assertThat(resultOrder.getBody().getItems()).hasSize(size);
  }

  /**
   * Update the entity.
   *
   * @param entity the entity
   * @param dataTable the example
   * @throws JSONException if errors occur
   */
  @When("^update the (\\w+)$")
  public void whenUpdate(final String entity, final DataTable dataTable) throws JSONException {
    var rows = dataTable.transpose().asList(String.class);
    switch (entity) {

      case "Category":
        categoryDto = TestUtils.createCategoryDto(categoryId, rows.get(0), rows.get(1));
        resultVoid = productApiService.updateCategory(categoryDto, categoryId);
        break;

      case "Product":
        upProductDto = TestUtils.createUpdatebleProductDto(productId, rows.get(0), rows.get(1),
            rows.get(2), rows.get(3), rows.get(4), rows.get(5), categoryId);
        resultVoid = productApiService.updateProduct(upProductDto, productId);
        break;

      case "Order":
        upOrderDto = TestUtils.createUpdatebleOrderDto(orderId, rows.get(0), rows.get(1));
        resultVoid = productApiService.patchOrder(upOrderDto, orderId);
        break;

      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Delete entity.
   *
   * @param entity the entity
   */
  @When("^delete the (\\w+)$")
  public void thenDelete(final String entity) {
    resultVoid = switch (entity) {
      case "Category" -> productApiService.deleteCategoryById(categoryId);
      case "Product" -> productApiService.deleteProductById(productId);
      case "Order" -> productApiService.deleteOrderById(orderId);
      case "OrderItem" -> productApiService.deleteOrderItemById(orderId, itemId);
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
    };
  }

  /**
   * Fetch entity from GET.
   *
   * @param entity the entity
   */
  @Then("^should be able to find that (\\w+)$")
  public void thenGet(final String entity) {
    switch (entity) {

      case "Category":
        var resultCat = productApiService.findCategories(null, null, Pageable.unpaged());
        assertThat(resultCat.getBody()).isNotEmpty();
        resultCategories = resultCat.getBody();
        break;

      case "Product":
        var resultPr = productApiService.findProducts(null, null, null, Pageable.unpaged());
        assertThat(resultPr.getBody()).isNotEmpty();
        resultProducts = resultPr.getBody();
        break;

      case "Order":
        var resultOrd = productApiService.findOrders(null, null, null, Pageable.unpaged());
        assertThat(resultOrd.getBody()).isNotEmpty();
        resultOrders = resultOrd.getBody();
        break;

      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Check permission of the user that should be not able to GET the entity.
   *
   * @param entity the entity
   */
  @Then("^should be not able to get that (\\w+)$")
  public void thenForbiddenGetBy(final String entity) {
    switch (entity) {

      case "Category":
        var resultCat = productApiService.findCategories(null, null, Pageable.unpaged());
        assertThat(resultCat.getBody()).isNotNull();
        resultCategories = resultCat.getBody();
        var optCat = resultCategories.stream()
            .filter(cat -> categoryId.equals(cat.getId())).findAny();
        assertThat(optCat).isEmpty();
        break;

      case "Product":
        var resultPr = productApiService.findProducts(null, null, null, Pageable.unpaged());
        assertThat(resultPr.getBody()).isNotNull();
        resultProducts = resultPr.getBody();
        var optPr = resultProducts.stream()
            .filter(prod -> productId.equals(prod.getId())).findAny();
        assertThat(optPr).isEmpty();
        break;

      case "Order":
        var resultOr = productApiService.findOrders(null, null, null, Pageable.unpaged());
        assertThat(resultOr.getBody()).isNotNull();
        resultOrders = resultOr.getBody();
        var optOr = resultOrders.stream().filter(order -> orderId.equals(order.getId())).findAny();
        assertThat(optOr).isEmpty();
        break;

      default:
        throw new IllegalArgumentException("Unexpected value: " + entity);
    }
  }

  /**
   * Stop and close all containers.
   */
  @AfterAll
  public static void endAll() {
    containers.stopAndCloseAll();
  }
}
