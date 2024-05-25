package it.francescofiora.product.itt.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Spring Glue.
 */
@CucumberContextConfiguration
@SpringBootTest
public class SpringGlue {

  protected static PostgreSQLContainer<?> postgreContainer;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreContainer::getUsername);
    registry.add("spring.datasource.password", postgreContainer::getPassword);
    registry.add("spring.datasource.driver-class-name", postgreContainer::getDriverClassName);
  }
}