package it.francescofiora.product.api.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.util.TestUtils;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class OrderEndToEndTest extends AbstractTestEndToEnd {

  private static final String CATEGORIES_URI = "/api/v1/categories";
  private static final String PRODUCTS_URI = "/api/v1/products";
  private static final String ORDERS_URI = "/api/v1/orders";
  private static final String ORDERS_ID_URI = "/api/v1/orders/%d";

  private static final String ALERT_CATEGORY_CREATED = "CategoryDto.created";
  private static final String ALERT_PRODUCT_CREATED = "ProductDto.created";

  private static final String ALERT_CREATED = "OrderDto.created";
  private static final String ALERT_DELETED = "OrderDto.deleted";
  private static final String ALERT_GET = "OrderDto.get";
  private static final String ALERT_CREATE_BAD_REQUEST = "NewOrderDto.badRequest";
  private static final String ALERT_CREATE_ITEM_BAD_REQUEST = "NewOrderItemDto.badRequest";

  private static final String ALERT_PATCHED = "OrderDto.patched";
  private static final String ALERT_PATCH_BAD_REQUEST = "OrderDto.badRequest";

  private static final String ALERT_NOT_FOUND = "OrderDto.notFound";
  private static final String ALERT_PRODUCT_NOT_FOUND = "ProductDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_CODE_NOT_BLANK = "[newOrderDto.code - NotBlank]";
  private static final String PARAM_CUSTOMER_NOT_NULL = "[newOrderDto.customer - NotNull]";
  private static final String PARAM_DATA_NOT_BLANK = "[newOrderDto.placedDate - NotNull]";
  private static final String PARAM_ITEMS_NOT_EMPTY = "[newOrderDto.items - NotEmpty]";
  private static final String PARAM_ITEM_NOT_NULL = "newOrderDto.items.item - NotNull";
  private static final String PARAM_QUANTITY_NULL = "[newOrderDto.items[0].quantity - Positive]";

  @Test
  void testCreate() {
    var categoryId = createAndReturnId(CATEGORIES_URI, TestUtils.createNewCategoryDto(),
        ALERT_CATEGORY_CREATED);

    var newProductDto = TestUtils.createNewProductDto();
    newProductDto.setCategory(TestUtils.createRefCategoryDto(categoryId));
    var productId = createAndReturnId(PRODUCTS_URI, newProductDto, ALERT_PRODUCT_CREATED);

    var newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(productId));
    var orderId = createAndReturnId(ORDERS_URI, newOrderDto, ALERT_CREATED);

    final var orderIdUri = String.format(ORDERS_ID_URI, orderId);

    var actual = get(orderIdUri, OrderDto.class, ALERT_GET, String.valueOf(orderId));
    assertThat(actual.getCode()).isEqualTo(newOrderDto.getCode());
    assertThat(actual.getCustomer()).isEqualTo(newOrderDto.getCustomer());
    assertThat(actual.getPlacedDate().truncatedTo(ChronoUnit.DAYS))
        .isEqualTo(newOrderDto.getPlacedDate().truncatedTo(ChronoUnit.DAYS));

    var orderDto = TestUtils.createUpdatebleOrderDto(orderId);
    patch(orderIdUri, orderDto, ALERT_PATCHED, String.valueOf(orderId));

    actual = get(orderIdUri, OrderDto.class, ALERT_GET, String.valueOf(orderId));
    assertThat(actual.getCode()).isEqualTo(orderDto.getCode());
    assertThat(actual.getCustomer()).isEqualTo(orderDto.getCustomer());
    assertThat(actual.getPlacedDate().truncatedTo(ChronoUnit.DAYS))
        .isEqualTo(orderDto.getPlacedDate().truncatedTo(ChronoUnit.DAYS));

    var orders =
        get(ORDERS_URI, PageRequest.of(1, 1), OrderDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(orders).isNotEmpty();
    var option = Stream.of(orders).filter(order -> order.getId().equals(orderId)).findAny();
    assertThat(option).isPresent().contains(actual);

    delete(orderIdUri, ALERT_DELETED, String.valueOf(orderId));

    assertGetNotFound(orderIdUri, OrderDto.class, ALERT_NOT_FOUND, String.valueOf(orderId));
  }

  @Test
  void testCreateBadRequest() {
    var newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(100L));
    assertCreateNotFound(ORDERS_URI, newOrderDto, ALERT_PRODUCT_NOT_FOUND,
        String.valueOf(newOrderDto.getItems().get(0).getProduct().getId()));

    var categoryId = createAndReturnId(CATEGORIES_URI, TestUtils.createNewCategoryDto(),
        ALERT_CATEGORY_CREATED);
    var newProductDto = TestUtils.createNewProductDto();
    newProductDto.setCategory(TestUtils.createRefCategoryDto(categoryId));
    var productId = createAndReturnId(PRODUCTS_URI, newProductDto, ALERT_PRODUCT_CREATED);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(productId));
    newOrderDto.setCode(null);
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_CODE_NOT_BLANK);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(productId));
    newOrderDto.setCode("");
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_CODE_NOT_BLANK);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(productId));
    newOrderDto.setCode("  ");
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_CODE_NOT_BLANK);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(productId));
    newOrderDto.setCustomer(null);
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_CUSTOMER_NOT_NULL);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(productId));
    newOrderDto.setPlacedDate(null);
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_DATA_NOT_BLANK);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_ITEMS_NOT_EMPTY);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(null);
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_ITEM_BAD_REQUEST,
        PARAM_ITEM_NOT_NULL);

    newOrderDto = TestUtils.createNewSimpleOrderDto();
    newOrderDto.getItems().add(TestUtils.createNewOrderItemDto(productId));
    newOrderDto.getItems().get(0).setQuantity(0);
    assertCreateBadRequest(ORDERS_URI, newOrderDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_QUANTITY_NULL);
  }

  @Test
  void testPatchBadRequest() {
    var orderDto = TestUtils.createUpdatebleOrderDto(1L);
    final var orderIdUri = String.format(ORDERS_ID_URI, 2L);
    assertPatchBadRequest(orderIdUri, orderDto, ALERT_PATCH_BAD_REQUEST, "1");
  }

  @Test
  void testUnauthorized() {
    testPostUnauthorized(ORDERS_URI, TestUtils.createNewOrderDto());

    testGetUnauthorized(ORDERS_URI);

    testGetUnauthorized(String.format(ORDERS_ID_URI, 1L));

    testDeleteUnauthorized(String.format(ORDERS_ID_URI, 1L));
  }

  @Test
  void testGetBadRequest() {
    assertGetBadRequest(ORDERS_URI + "/999999999999999999999999", String.class,
        "id.badRequest", PARAM_NOT_VALID_LONG);
  }
}
