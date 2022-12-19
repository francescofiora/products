package it.francescofiora.product.client.impl;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
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
 * OrderClientService Test.
 */
class OrderClientServiceTest extends AbstractTestClientService {

  @BeforeAll
  static void setUpMockWebServer() throws IOException {
    final Dispatcher dispatcher = new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) {
        switch (request.getPath()) {
          case "/api/v1/orders":
            return createMockResponse("");
          case "/api/v1/orders/1":
            return createMockResponse("");
          case "/api/v1/orders/1/items":
            return createMockResponse("");
          case "/api/v1/orders/1/items/1":
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
    var orderClientService = new OrderClientServiceImpl(createClientInfo());
    var result = orderClientService.create(new NewOrderDto()).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testPatch() {
    var order = new UpdatebleOrderDto();
    order.setId(1L);
    var orderClientService = new OrderClientServiceImpl(createClientInfo());
    var result = orderClientService.patch(order).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testFindAll() {
    var orderClientService = new OrderClientServiceImpl(createClientInfo());
    var result = orderClientService.findAll(Pageable.unpaged()).collectList().block();
    assertThat(result).isNotNull();
  }

  @Test
  void testFindOne() {
    var orderClientService = new OrderClientServiceImpl(createClientInfo());
    var result = orderClientService.findOne(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDelete() {
    var orderClientService = new OrderClientServiceImpl(createClientInfo());
    var result = orderClientService.delete(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testAddOrderItem() {
    var orderClientService = new OrderClientServiceImpl(createClientInfo());
    var result = orderClientService.addOrderItem(1L, new NewOrderItemDto()).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDeleteOrderItem() {
    var orderClientService = new OrderClientServiceImpl(createClientInfo());
    var result = orderClientService.deleteOrderItem(1L, 1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
