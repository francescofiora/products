package it.francescofiora.product.itt.api.util;

import it.francescofiora.product.itt.ssl.CertificateGenerator;
import lombok.Getter;
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

  private Network network = Network.newNetwork();
  private CertificateGenerator generator;
  private final boolean useSsl;

  @Getter
  private String tmpDir = "";

  /**
   * Use Ssl.
   *
   * @throws Exception if errors occur
   */
  public void useSsl() throws Exception {
    if (!useSsl) {
      throw new Exception("useSsl is false");
    }

    tmpDir = UtilResource.getResourceFile("ssl");
    generator = new CertificateGenerator(tmpDir, "ca.francescofiora.it");
    generator.clean();
    generator.generateRoot();
    generator.generateSignedCertificate(PRODUCT_API, "01", true);
  }

  /**
   * Create PostgreSql Container.
   *
   * @return PostgreSQLContainer
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

  public GenericContainer<?> createContainer(String dockerImageName) {
    return new GenericContainer<>(dockerImageName).withNetwork(network);
  }

  public void endUseSsl() {
    generator.clean();
  }
}
