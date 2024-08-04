package it.francescofiora.product.eureka.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

/**
 * Api Test.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class ApiTest {

  @Autowired
  private WebApplicationContext wac;

  private WebTestClient client;

  @BeforeEach
  void setUp() {
    client = MockMvcWebTestClient.bindToApplicationContext(this.wac).build();
  }

  @Test
  void testActuator() {
    client.get().uri("/actuator/info").exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.build.name").isEqualTo("product-eureka");

    client.get().uri("/actuator/health").exchange()
        .expectStatus().isOk()
        .expectBody().json("""
            {"status":"UP","groups":["liveness","readiness"]}
            """);
  }
}
