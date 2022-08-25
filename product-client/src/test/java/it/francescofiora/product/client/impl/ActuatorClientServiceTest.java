package it.francescofiora.product.client.impl;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.client.ActuatorClientService;
import java.io.IOException;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * ActuatorClientService Test.
 */
class ActuatorClientServiceTest extends AbstractTestClientService {

  private ActuatorClientService actuatorClientService;

  private static final String BODY_INFO = "{ \"UP\" }";
  private static final String BODY_HEALTH = "{ \"HEALTH\" }";

  @BeforeAll
  public static void setUpMockWebServer() throws IOException {
    final Dispatcher dispatcher = new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) {
        switch (request.getPath()) {
          case "/actuator/info":
            return createMockResponse(BODY_INFO);
          case "/actuator/health":
            return createMockResponse(BODY_HEALTH);
          default:
            return new MockResponse().setResponseCode(HttpStatus.BAD_REQUEST.value());
        }
      }
    };

    startServer(dispatcher);
  }

  @BeforeEach
  public void setUp() {
    actuatorClientService = new ActuatorClientServiceImpl(createClientInfo());
  }

  @AfterAll
  static void tearDown() throws IOException {
    stopServer();
  }

  @Test
  void testGetInfo() {
    var result = actuatorClientService.getInfo().block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isEqualTo(BODY_INFO);
  }

  @Test
  void testGetHealth() {
    var result = actuatorClientService.getHealth().block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isEqualTo(BODY_HEALTH);
  }
}
