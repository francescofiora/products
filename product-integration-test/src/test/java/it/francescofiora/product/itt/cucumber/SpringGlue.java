package it.francescofiora.product.itt.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Spring Glue.
 */
@CucumberContextConfiguration
@SpringBootTest
public class SpringGlue {

  protected static PostgreSQLContainer<?> postgreContainer;
  protected static GenericContainer<?> eureka;

  protected static final String EUREKA_USER = "eurekaUser";
  protected static final String EUREKA_PASSWORD = "eurekaPassword";

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreContainer::getUsername);
    registry.add("spring.datasource.password", postgreContainer::getPassword);
    registry.add("spring.datasource.driver-class-name", postgreContainer::getDriverClassName);
    registry.add("eureka.client.serviceUrl.defaultZone", SpringGlue::getEurekaPath);
  }

  private static String getEurekaPath() {
    return "http://" + EUREKA_USER + ":" + EUREKA_PASSWORD + "@"
        + eureka.getHost() + ":" + eureka.getFirstMappedPort() + "/eureka";
  }
}
