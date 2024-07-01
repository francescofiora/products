package it.francescofiora.product.api.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.api.config.SecurityConfig;
import it.francescofiora.product.api.service.OrderService;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.OrderItemDto;
import it.francescofiora.product.api.util.TestUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderApi.class)
@Import({BuildProperties.class, SecurityConfig.class})
class OrderApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final Long ITEM_ID = 10L;
  private static final String ORDERS_URI = "/api/v1/orders";
  private static final String ORDERS_ID_URI = "/api/v1/orders/{id}";
  private static final String ORDERS_ID_ITEMS_URI = "/api/v1/orders/{id}/items";
  private static final String ORDERS_ID_ITEMS_ID_URI =
      "/api/v1/orders/{order_id}/items/{order_item_id}";
  private static final String WRONG_URI = "/api/v1/wrong";

  @MockBean
  private OrderService orderService;

  @Test
  void testCreate() throws Exception {
    var newOrderDto = TestUtils.createNewOrderDto();

    var orderDto = new OrderDto();
    orderDto.setId(ID);
    given(orderService.create(any(NewOrderDto.class))).willReturn(orderDto);

    var result =
        performPost(ORDERS_URI, newOrderDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(ORDERS_URI + "/" + ID);
  }

  @Test
  void testCreateForbidden() throws Exception {
    var newOrderDto = TestUtils.createNewOrderDto();
    performPostForbidden(ORDERS_URI, newOrderDto).andExpect(status().isUnauthorized());
  }

  @Test
  void testCreateBadRequest() throws Exception {
    // Code
    var newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCode(null);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCode("");
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCode("  ");
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Customer
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCustomer(null);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // PlacedDate
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setPlacedDate(null);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Items
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setItems(null);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setItems(Collections.emptyList());
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Items->Quantity
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).setQuantity(null);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).setQuantity(0);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Items->Product
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).setProduct(null);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).getProduct().setId(null);
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    // id
    var orderDto = TestUtils.createUpdatebleOrderDto(null);
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    // code
    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCode(null);
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCode("");
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCode("  ");
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    // Customer
    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCustomer(null);
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    // PlacedDate
    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setPlacedDate(null);
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdate() throws Exception {
    var orderDto = TestUtils.createUpdatebleOrderDto(ID);
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isOk());
  }

  @Test
  void testUpdateForbidden() throws Exception {
    var orderDto = TestUtils.createUpdatebleOrderDto(ID);

    performPatchForbidden(ORDERS_ID_URI, ID, orderDto).andExpect(status().isUnauthorized());
  }

  @Test
  void testGetAll() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findAll(any(), any(), any(), any(Pageable.class)))
        .willReturn(new PageImpl<OrderDto>(List.of(expected)));

    var result = performGet(ORDERS_URI, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<OrderDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetAllForbidden() throws Exception {
    var pageable = PageRequest.of(1, 1);

    performGetForbidden(ORDERS_URI, pageable).andExpect(status().isUnauthorized());
  }

  @Test
  void testGetOrder() throws Exception {
    var expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findOne(ID)).willReturn(Optional.of(expected));
    var result = performGet(ORDERS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<OrderDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testGetForbidden() throws Exception {
    performGetForbidden(ORDERS_ID_URI, ID).andExpect(status().isUnauthorized());
  }

  @Test
  void testDeleteOrder() throws Exception {
    performDelete(ORDERS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testDeleteForbidden() throws Exception {
    performDeleteForbidden(ORDERS_ID_URI, ID).andExpect(status().isUnauthorized());
  }

  @Test
  void testAddItem() throws Exception {
    var newOrderItemDto = TestUtils.createNewOrderItemDto();
    var orderItemDto = new OrderItemDto();
    orderItemDto.setId(ITEM_ID);
    given(orderService.addOrderItem(eq(ID), any(NewOrderItemDto.class))).willReturn(orderItemDto);
    var result = performPost(ORDERS_ID_ITEMS_URI, ID, newOrderItemDto)
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(ORDERS_URI + "/" + ID + "/items/" + ITEM_ID);
  }

  @Test
  void testAddItemForbidden() throws Exception {
    var newOrderItemDto = TestUtils.createNewOrderItemDto();

    performPostForbidden(ORDERS_ID_ITEMS_URI, ID, newOrderItemDto)
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testAddItemBadRequest() throws Exception {
    // Items->Quantity
    var orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setQuantity(null);
    performPost(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setQuantity(0);
    performPost(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    // Items->Product
    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setProduct(null);
    performPost(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.getProduct().setId(null);
    performPost(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteItem() throws Exception {
    performDelete(ORDERS_ID_ITEMS_ID_URI, ID, ITEM_ID).andExpect(status().isNoContent());
  }

  @Test
  void testDeleteItemForbidden() throws Exception {
    performDeleteForbidden(ORDERS_ID_ITEMS_ID_URI, ID, ITEM_ID)
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
