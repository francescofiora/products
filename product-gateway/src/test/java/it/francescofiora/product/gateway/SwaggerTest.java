package it.francescofiora.product.gateway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Swagger UI Page and JSON Test.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application_test.properties"})
class SwaggerTest {

  @Autowired
  private WebTestClient webClient;

  @Value("${spring.security.user.name}")
  private String gatewayUser;

  @Value("${spring.security.user.password}")
  private String gatewayPassword;

  @Test
  void testSwaggerUiPage() {
    webClient.get().uri("/v3/api-docs")
        .headers(headers -> headers.setBasicAuth(gatewayUser, gatewayPassword))
        .exchange()
        .expectStatus().isOk();
  }
}
