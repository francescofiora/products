package it.francescofiora.product.api.web.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Prometheus Test.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application_test.properties"})
class ActuatorTest extends AbstractApiTest {

  @Test
  void testEndPoint() throws Exception {
    performGetWithNoUser("/actuator/info")
        .andExpect(status().isOk()).andExpect(content().string(containsString("product-api")));

    performGetWithNoUser("/actuator/health")
        .andExpect(status().isOk()).andExpect(content().string(containsString("UP")));
  }
}
