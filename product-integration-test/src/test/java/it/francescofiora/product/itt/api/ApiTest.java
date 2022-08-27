package it.francescofiora.product.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import it.francescofiora.product.itt.api.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Product Integration Test.
 */
@Slf4j
class ApiTest extends AbstractTestContainer {

  private static final String DATASOURCE_URL = "jdbc:postgresql://postgre:5432/db_product";

  private static PostgreSQLContainer<?> postgreContainer;
  private static GenericContainer<?> product;

  private static ActuatorClientService actuatorClientService;
  private static CategoryClientService categoryClientService;
  private static ProductClientService productClientService;
  private static OrderClientService orderClientService;

  private static StartStopContainers containers = new StartStopContainers();

  @BeforeAll
  public static void init() throws Exception {
    postgreContainer = createPostgreSqlContainer();
    containers.add(postgreContainer);

    try (var ds = createHikariDataSource(postgreContainer)) {
      executeQuery(ds, "CREATE SCHEMA IF NOT EXISTS STORE");
    }

    // @formatter:off
    product = createContainer("francescofiora-product")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withNetworkAliases("product")
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

  @Test
  void testHealth() throws Exception {
    assertTrue(containers.areRunning());
    System.out.println("--- test Health ---");
    var result = actuatorClientService.getHealth().block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    result = actuatorClientService.getInfo().block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    System.out.println(result.getBody());
  }

  private Long validateResponseAndGetId(ResponseEntity<Void> result) {
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertNotNull(result.getHeaders().get(HttpHeaders.LOCATION));
    var url = result.getHeaders().get(HttpHeaders.LOCATION).get(0);
    assertNotNull(url);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  @Test
  void testCategory() throws Exception {
    var newCategoryDto = TestUtils.createNewCategoryDto();
    var resultPost = categoryClientService.create(newCategoryDto).block();
    var categoryId = validateResponseAndGetId(resultPost);

    var resultGet = categoryClientService.findOne(categoryId).block();
    assertThat(resultGet.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(resultGet.getBody());
    assertThat(resultGet.getBody().getName()).isEqualTo(newCategoryDto.getName());
    assertThat(resultGet.getBody().getDescription()).isEqualTo(newCategoryDto.getDescription());

    var categoryDto = TestUtils.createCategoryDto(categoryId);
    resultPost = categoryClientService.update(categoryDto).block();
    assertThat(resultPost.getStatusCode()).isEqualTo(HttpStatus.OK);

    var stream = categoryClientService.findAll(Pageable.unpaged()).toStream();
    var opt = stream.filter(cat -> categoryId.equals(cat.getId())).findAny();
    assertThat(opt).hasValue(categoryDto);

    var resultDel = categoryClientService.delete(categoryId).block();
    assertThat(resultDel.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    stream = categoryClientService.findAll(Pageable.unpaged()).toStream();
    opt = stream.filter(cat -> categoryId.equals(cat.getId())).findAny();
    assertThat(opt).isEmpty();
  }

  @Test
  void testProduct() throws Exception {
    var resultPost = categoryClientService.create(TestUtils.createNewCategoryDto()).block();
    var categoryId = validateResponseAndGetId(resultPost);

    var newProductDto = TestUtils.createNewProductDto(categoryId);
    resultPost = productClientService.create(newProductDto).block();
    var productId = validateResponseAndGetId(resultPost);

    var resultGet = productClientService.findOne(productId).block();
    assertThat(resultGet.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(resultGet.getBody());
    assertThat(resultGet.getBody().getName()).isEqualTo(newProductDto.getName());
    assertThat(resultGet.getBody().getDescription()).isEqualTo(newProductDto.getDescription());

    var productDto = TestUtils.createUpdatebleProductDto(productId, categoryId);
    resultPost = productClientService.update(productDto).block();
    assertThat(resultPost.getStatusCode()).isEqualTo(HttpStatus.OK);

    var stream = productClientService.findAll(Pageable.unpaged()).toStream();
    var opt = stream.filter(prod -> productId.equals(prod.getId())).findAny();
    assertThat(opt).isNotEmpty();
    assertThat(opt.get().getName()).isEqualTo(productDto.getName());
    assertThat(opt.get().getDescription()).isEqualTo(productDto.getDescription());

    var resultDel = productClientService.delete(productId).block();
    assertThat(resultDel.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    stream = productClientService.findAll(Pageable.unpaged()).toStream();
    opt = stream.filter(prod -> productId.equals(prod.getId())).findAny();
    assertThat(opt).isEmpty();
  }

  @Test
  void testOrder() throws Exception {
    var resultPost = categoryClientService.create(TestUtils.createNewCategoryDto()).block();
    var categoryId = validateResponseAndGetId(resultPost);

    var newProductDto = TestUtils.createNewProductDto(categoryId);
    resultPost = productClientService.create(newProductDto).block();
    var productId = validateResponseAndGetId(resultPost);

    var newOrderDto = TestUtils.createNewOrderDto(productId);
    resultPost = orderClientService.create(newOrderDto).block();
    var orderId = validateResponseAndGetId(resultPost);

    var resultGet = orderClientService.findOne(orderId).block();
    assertThat(resultGet.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertNotNull(resultGet.getBody());
    assertThat(resultGet.getBody().getCode()).isEqualTo(newOrderDto.getCode());
    assertThat(resultGet.getBody().getCustomer()).isEqualTo(newOrderDto.getCustomer());

    var orderDto = TestUtils.createUpdatebleOrderDto(orderId);
    resultPost = orderClientService.patch(orderDto).block();
    assertThat(resultPost.getStatusCode()).isEqualTo(HttpStatus.OK);

    newProductDto = TestUtils.createNewProductDto2(categoryId);
    resultPost = productClientService.create(newProductDto).block();
    productId = validateResponseAndGetId(resultPost);
    var newOrderItemDto = TestUtils.createNewOrderItemDto(productId);
    resultPost = orderClientService.addOrderItem(orderId, newOrderItemDto).block();
    assertThat(resultPost.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    var itemId = validateResponseAndGetId(resultPost);

    var stream = orderClientService.findAll(Pageable.unpaged()).toStream();
    var opt = stream.filter(order -> orderId.equals(order.getId())).findAny();
    assertThat(opt).isNotEmpty();
    assertThat(opt.get().getCode()).isEqualTo(orderDto.getCode());
    assertThat(opt.get().getCustomer()).isEqualTo(orderDto.getCustomer());
    assertThat(opt.get().getItems()).hasSize(2);

    var resultDel = orderClientService.deleteOrderItem(orderId, itemId).block();
    assertThat(resultDel.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    stream = orderClientService.findAll(Pageable.unpaged()).toStream();
    opt = stream.filter(order -> orderId.equals(order.getId())).findAny();
    assertThat(opt.get().getItems()).hasSize(1);

    resultDel = orderClientService.delete(orderId).block();
    assertThat(resultDel.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    stream = orderClientService.findAll(Pageable.unpaged()).toStream();
    opt = stream.filter(order -> orderId.equals(order.getId())).findAny();
    assertThat(opt).isEmpty();
  }

  @AfterAll
  public static void endAll() {
    containers.stopAndCloseAll();
  }
}
