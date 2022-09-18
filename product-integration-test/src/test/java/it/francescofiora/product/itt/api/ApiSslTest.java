package it.francescofiora.product.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
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
import it.francescofiora.product.itt.api.util.ContainerGenerator;
import it.francescofiora.product.itt.api.util.TestUtils;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Slf4j
class ApiSslTest extends AbstractTestContainer {

  private static final String DATASOURCE_URL =
      "jdbc:postgresql://product-postgresql:5432/db_product";

  private static PostgreSQLContainer<?> postgreContainer;
  private static GenericContainer<?> product;

  private static ActuatorClientService actuatorClientService;
  private static CategoryClientService categoryClientService;
  private static ProductClientService productClientService;
  private static OrderClientService orderClientService;

  private static StartStopContainers containers = new StartStopContainers();

  private static ContainerGenerator containerGenerator = new ContainerGenerator(true);

  @BeforeAll
  public static void init() throws Exception {
    containerGenerator.useSsl();

    postgreContainer = containerGenerator.createPostgreSqlContainer();
    containers.add(postgreContainer);

    try (var ds = createHikariDataSource(postgreContainer)) {
      executeQuery(ds, "CREATE SCHEMA IF NOT EXISTS STORE");
    }

    // @formatter:off
    product = containerGenerator.createContainer("francescofiora-product")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("SSL_ENABLED", "true")
        .withEnv("KEYSTORE_PASSWORD", "mypass")
        .withEnv("KEYSTORE_FILE", "/workspace/config/product-keystore.jks")
        .withEnv("TRUSTSTORE_PASSWORD", "mypass")
        .withEnv("TRUSTSTORE_FILE", "/workspace/config/truststore.ts")
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withNetworkAliases(ContainerGenerator.PRODUCT_API)
        .withExposedPorts(8081);
    // @formatter:on
    var tmpDir = containerGenerator.getTmpDir();
    product.addFileSystemBind(tmpDir + File.separator + "product-keystore.jks",
        "/workspace/config/product-keystore.jks", BindMode.READ_ONLY);
    product.addFileSystemBind(tmpDir + File.separator + "truststore.ts",
        "/workspace/config/truststore.ts", BindMode.READ_ONLY);
    containers.add(product);

    var adminProp = new ProductClientProperties();
    adminProp.setBaseUrl("https://" + product.getHost() + ":" + product.getFirstMappedPort());
    adminProp.setUserName("admin");
    adminProp.setPassword("password");
    adminProp.setSslEnabled(true);

    var userProp = new ProductClientProperties();
    userProp.setBaseUrl("https://" + product.getHost() + ":" + product.getFirstMappedPort());
    userProp.setUserName("user");
    userProp.setPassword("password");
    userProp.setSslEnabled(true);

    var sslContext =
        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

    actuatorClientService = new ActuatorClientServiceImpl(userProp) {
      @Override
      protected SslContext createSslContext() {
        return sslContext;
      }
    };
    categoryClientService = new CategoryClientServiceImpl(adminProp) {
      @Override
      protected SslContext createSslContext() {
        return sslContext;
      }
    };
    productClientService = new ProductClientServiceImpl(adminProp) {
      @Override
      protected SslContext createSslContext() {
        return sslContext;
      }
    };
    orderClientService = new OrderClientServiceImpl(userProp) {
      @Override
      protected SslContext createSslContext() {
        return sslContext;
      }
    };
  }

  @Test
  void testHealth() throws Exception {
    assertTrue(containers.areRunning());

    var result = actuatorClientService.getHealth().block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    result = actuatorClientService.getInfo().block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
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
