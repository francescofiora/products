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
import it.francescofiora.product.client.ActuatorClientService;
import it.francescofiora.product.client.CategoryClientService;
import it.francescofiora.product.client.OrderClientService;
import it.francescofiora.product.client.ProductClientService;
import it.francescofiora.product.client.impl.ActuatorClientServiceImpl;
import it.francescofiora.product.client.impl.CategoryClientServiceImpl;
import it.francescofiora.product.client.impl.OrderClientServiceImpl;
import it.francescofiora.product.client.impl.ProductClientServiceImpl;
import it.francescofiora.product.itt.ProductClientProperties;
import it.francescofiora.product.itt.StartStopContainers;
import it.francescofiora.product.itt.api.AbstractTestContainer;
import it.francescofiora.product.itt.api.util.ContainerGenerator;
import it.francescofiora.product.itt.api.util.TestUtils;
import java.sql.SQLException;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Cucumber Step Definitions.
 */
@Slf4j
public class StepDefinitions extends AbstractTestContainer {

  private static final String DATASOURCE_URL =
      "jdbc:postgresql://product-postgresql:5432/db_product";

  private static PostgreSQLContainer<?> postgreContainer;
  private static GenericContainer<?> product;
  private static GenericContainer<?> eureka;

  private static ActuatorClientService actuatorClientService;
  private static CategoryClientService categoryClientService;
  private static ProductClientService productClientService;
  private static OrderClientService orderClientService;

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
  private static Stream<CategoryDto> resultCategories;
  private static Stream<ProductDto> resultProducts;
  private static Stream<OrderDto> resultOrders;
  private static Long categoryId;
  private static Long productId;
  private static Long orderId;
  private static Long itemId;
  private static HttpStatusCode lastStatusCode;

  /**
   * Start all containers.
   *
   * @throws JSONException if errors occur
   * @throws SQLException if errors occur
   */
  @BeforeAll
  public static void init() throws JSONException, SQLException {
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

    // @formatter:off
    product = containerGenerator.createContainer("francescofiora-product")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withNetworkAliases(ContainerGenerator.PRODUCT_API)
        .withExposedPorts(8081);
    // @formatter:on
    containers.add(product);

    var adminProp = new ProductClientProperties();
    adminProp.setBaseUrl("http://" + product.getHost() + ":" + product.getFirstMappedPort());
    adminProp.setUserName("admin");
    adminProp.setPassword("password");

    var userProp = new ProductClientProperties();
    userProp.setBaseUrl("http://" + product.getHost() + ":" + product.getFirstMappedPort());
    userProp.setUserName("user");
    userProp.setPassword("password");

    actuatorClientService = new ActuatorClientServiceImpl(userProp);
    categoryClientService = new CategoryClientServiceImpl(adminProp);
    productClientService = new ProductClientServiceImpl(adminProp);
    orderClientService = new OrderClientServiceImpl(userProp);
  }

  @Given("the system up and running")
  public void givenSystemUp() {
    assertTrue(containers.areRunning());
  }

  /**
   * When GET the Application Health/Info.
   *
   * @param op Health or Info
   */
  @When("^GET the Application (\\w+)$")
  public void whenGetApplication(final String op) {
    var resultString = switch (op) {
      case "Health" -> actuatorClientService.getHealth().block();
      case "Info" -> actuatorClientService.getInfo().block();
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
        var resultCat = categoryClientService.create(newCategoryDto).block();
        categoryId = validateResponseAndGetId(resultCat);
        newProductDto = TestUtils.createNewProductDto(rows.get(0), rows.get(1), rows.get(2),
            rows.get(3), rows.get(4), rows.get(5), categoryId);
        break;

      case "Order":
        newOrderDto = TestUtils.createNewOrderDto(rows.get(0), rows.get(1));
        newOrderDto.getItems().add(newOrderItemDto);
        break;

      case "OrderItem":
        var resultPr = productClientService.create(newProductDto).block();
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
        resultVoid = categoryClientService.create(newCategoryDto).block();
        categoryId = validateResponseAndGetId(resultVoid);
        break;

      case "Product":
        resultVoid = productClientService.create(newProductDto).block();
        productId = validateResponseAndGetId(resultVoid);
        break;

      case "Order":
        resultVoid = orderClientService.create(newOrderDto).block();
        orderId = validateResponseAndGetId(resultVoid);
        break;

      case "OrderItem":
        resultVoid = orderClientService.addOrderItem(orderId, newOrderItemDto).block();
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
        resultCategory = categoryClientService.findOne(categoryId).block();
        lastStatusCode = resultCategory.getStatusCode();
        break;

      case "Product":
        resultProduct = productClientService.findOne(productId).block();
        lastStatusCode = resultProduct.getStatusCode();
        break;

      case "Order":
        resultOrder = orderClientService.findOne(orderId).block();
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
          var opt = resultCategories.filter(cat -> categoryId.equals(cat.getId())).findAny();
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
          var opt = resultProducts.filter(prod -> productId.equals(prod.getId())).findAny();
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
          var opt = resultOrders.filter(order -> orderId.equals(order.getId())).findAny();
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
        resultVoid = categoryClientService.update(categoryDto).block();
        break;

      case "Product":
        upProductDto = TestUtils.createUpdatebleProductDto(productId, rows.get(0), rows.get(1),
            rows.get(2), rows.get(3), rows.get(4), rows.get(5), categoryId);
        resultVoid = productClientService.update(upProductDto).block();
        break;

      case "Order":
        upOrderDto = TestUtils.createUpdatebleOrderDto(orderId, rows.get(0), rows.get(1));
        resultVoid = orderClientService.patch(upOrderDto).block();
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
      case "Category" -> categoryClientService.delete(categoryId).block();
      case "Product" -> productClientService.delete(productId).block();
      case "Order" -> orderClientService.delete(orderId).block();
      case "OrderItem" -> orderClientService.deleteOrderItem(orderId, itemId).block();
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
        resultCategories = categoryClientService.findAll(Pageable.unpaged()).toStream();
        break;

      case "Product":
        resultProducts = productClientService.findAll(Pageable.unpaged()).toStream();
        break;

      case "Order":
        resultOrders = orderClientService.findAll(Pageable.unpaged()).toStream();
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
        resultCategories = categoryClientService.findAll(Pageable.unpaged()).toStream();
        var optCat = resultCategories.filter(cat -> categoryId.equals(cat.getId())).findAny();
        assertThat(optCat).isEmpty();
        break;

      case "Product":
        resultProducts = productClientService.findAll(Pageable.unpaged()).toStream();
        var optPr = resultProducts.filter(prod -> productId.equals(prod.getId())).findAny();
        assertThat(optPr).isEmpty();
        break;

      case "Order":
        resultOrders = orderClientService.findAll(Pageable.unpaged()).toStream();
        var optOr = resultOrders.filter(order -> orderId.equals(order.getId())).findAny();
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
