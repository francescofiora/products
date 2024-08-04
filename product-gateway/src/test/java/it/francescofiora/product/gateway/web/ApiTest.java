package it.francescofiora.product.gateway.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Api Test.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class ApiTest {

  @Autowired
  private WebTestClient client;

  @Test
  void testActuator() {
    client.get().uri("/actuator/info").exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.build.name").isEqualTo("product-gateway");

    client.get().uri("/actuator/health").exchange()
        .expectStatus().isOk()
        .expectBody().json("""
            {"status":"UP","groups":["liveness","readiness"]}
            """);
  }
}
