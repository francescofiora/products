package it.francescofiora.product.itt.util;

import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Container Generator.
 */
@RequiredArgsConstructor
public class ContainerGenerator {

  public static final String PRODUCT_POSTGRESQL = "product-postgresql";
  public static final String PRODUCT_API = "product";
  public static final String PRODUCT_EUREKA = "product-eureka";

  private final Network network = Network.newNetwork();

  /**
   * Create PostgreSql Container.
   *
   * @return the PostgreSQLContainer
   */
  public PostgreSQLContainer<?> createPostgreSqlContainer() {
    // @formatter:off
    var postgres = new PostgreSQLContainer<>("postgres:14.1")
        .withNetwork(network)
        .withNetworkAliases(PRODUCT_POSTGRESQL)
        .withUsername("product").withPassword("secret")
        .withDatabaseName("db_product");
    // @formatter:on
    return postgres;
  }

  /**
   * Create EurekaServer Container.
   *
   * @return the EurekaServer Container
   */
  public GenericContainer<?> createEurekaServerContainer() {
    // @formatter:off
    var eureka = createContainer("francescofiora-product-eureka")
        .withNetworkAliases(PRODUCT_EUREKA)
        .withExposedPorts(8761);
    // @formatter:on
    return eureka;
  }

  public GenericContainer<?> createContainer(String dockerImageName) {
    return new GenericContainer<>(dockerImageName).withNetwork(network);
  }
}
