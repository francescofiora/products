package it.francescofiora.product.client.impl;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import java.io.IOException;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

/**
 * ProductClientService Test.
 */
class ProductClientServiceTest extends AbstractTestClientService {

  @BeforeAll
  static void setUpMockWebServer() throws IOException {
    final Dispatcher dispatcher = new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) {
        switch (request.getPath()) {
          case "/api/v1/products":
            return createMockResponse("");
          case "/api/v1/products/1":
            return createMockResponse("");
          default:
            return createMockBadRequestResponse();
        }
      }
    };

    startServer(dispatcher);
  }

  @AfterAll
  static void tearDown() throws IOException {
    stopServer();
  }

  @Test
  void testCreate() {
    var productClientService = new ProductClientServiceImpl(createClientInfo());
    var result = productClientService.create(new NewProductDto()).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testUpdate() {
    var product = new UpdatebleProductDto();
    product.setId(1L);
    var productClientService = new ProductClientServiceImpl(createClientInfo());
    var result = productClientService.update(product).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testFindAll() {
    var productClientService = new ProductClientServiceImpl(createClientInfo());
    var result = productClientService.findAll(Pageable.unpaged()).collectList().block();
    assertThat(result).isNotNull();
  }

  @Test
  void testFindOne() {
    var productClientService = new ProductClientServiceImpl(createClientInfo());
    var result = productClientService.findOne(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDelete() {
    var productClientService = new ProductClientServiceImpl(createClientInfo());
    var result = productClientService.delete(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
