package it.francescofiora.product.itt.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.francescofiora.product.client.ProductApiService;
import it.francescofiora.product.itt.StartStopContainers;
import it.francescofiora.product.itt.api.AbstractTestContainer;
import it.francescofiora.product.itt.api.util.ContainerGenerator;
import it.francescofiora.product.itt.component.CategoryComponent;
import it.francescofiora.product.itt.component.OrderComponent;
import it.francescofiora.product.itt.component.ProductComponent;
import java.sql.SQLException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
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

  private static ResponseEntity<String> resultString;

  @Autowired
  private DiscoveryClient discoveryClient;

  @Autowired
  private ProductApiService productApiService;

  @Autowired
  private CategoryComponent categoryComponent;

  @Autowired
  private ProductComponent productComponent;

  @Autowired
  private OrderComponent orderComponent;

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
        Thread.sleep(500);
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
  @Then("the result should contain {string}")
  public void thenResultContains(final String expected) {
    assertThat(resultString.getBody()).contains(expected);
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
  @When("^create that (\\w+)$")
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
  @Then("^should be able to get that (\\w+)$")
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
  @Then("^the (\\w+) from (\\w+) should the same as from (\\w+)$")
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

  @Then("that Order should have {int} items")
  public void checkItems(int size) {
    orderComponent.checkSizeItems(size);
  }

  /**
   * Update the entity.
   *
   * @param entity the entity
   * @param dataTable the example
   */
  @When("^update the (\\w+)$")
  public void whenUpdate(final String entity, final DataTable dataTable) {
    var rows = dataTable.transpose().asList(String.class);
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
  @When("^delete the (\\w+)$")
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
  @When("^get all (\\w+)$")
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
  @Then("^that (\\w+) should be not present$")
  public void thenNotExist(final String entity) {
    switch (entity) {
      case "Category" -> categoryComponent.checkCategoryNotExist();
      case "Product" -> productComponent.checkProductNotExist();
      case "Order" -> orderComponent.checkOrderNotExist();
      default -> throw new IllegalArgumentException("Unexpected value: " + entity);
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
