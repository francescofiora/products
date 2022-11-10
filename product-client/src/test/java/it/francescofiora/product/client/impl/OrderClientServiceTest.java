package it.francescofiora.product.client.impl;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.client.OrderClientService;
import java.io.IOException;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

/**
 * OrderClientService Test.
 */
class OrderClientServiceTest extends AbstractTestClientService {

  private OrderClientService orderClientService;

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

  @BeforeEach
  void setUp() {
    orderClientService = new OrderClientServiceImpl(createClientInfo());
  }

  @AfterAll
  static void tearDown() throws IOException {
    stopServer();
  }

  @Test
  void testCreate() {
    var result = orderClientService.create(new NewOrderDto()).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testPatch() {
    var order = new UpdatebleOrderDto();
    order.setId(1L);
    var result = orderClientService.patch(order).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testFindAll() {
    var result = orderClientService.findAll(Pageable.unpaged()).collectList().block();
    assertThat(result).isNotNull();
  }

  @Test
  void testFindOne() {
    var result = orderClientService.findOne(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDelete() {
    var result = orderClientService.delete(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testAddOrderItem() {
    var result = orderClientService.addOrderItem(1L, new NewOrderItemDto()).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDeleteOrderItem() {
    var result = orderClientService.deleteOrderItem(1L, 1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
