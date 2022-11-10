package it.francescofiora.product.client.impl;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.client.CategoryClientService;
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
 * CategoryClientService Test.
 */
class CategoryClientServiceTest extends AbstractTestClientService {

  private CategoryClientService categoryClientService;

  @BeforeAll
  static void setUpMockWebServer() throws IOException {
    final Dispatcher dispatcher = new Dispatcher() {
      @Override
      public MockResponse dispatch(RecordedRequest request) {
        switch (request.getPath()) {
          case "/api/v1/categories":
            return createMockResponse("");
          case "/api/v1/categories/1":
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
    categoryClientService = new CategoryClientServiceImpl(createClientInfo());
  }

  @AfterAll
  static void tearDown() throws IOException {
    stopServer();
  }

  @Test
  void testCreate() {
    var result = categoryClientService.create(new NewCategoryDto()).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testUpdate() {
    var category = new CategoryDto();
    category.setId(1L);
    var result = categoryClientService.update(category).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testFindAll() {
    var result = categoryClientService.findAll(Pageable.unpaged()).collectList().block();
    assertThat(result).isNotNull();
  }

  @Test
  void testFindOne() {
    var result = categoryClientService.findOne(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDelete() {
    var result = categoryClientService.delete(1L).block();
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
