package it.francescofiora.product.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.config.MethodSecurityConfig;
import it.francescofiora.product.service.OrderService;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.OrderDto;
import it.francescofiora.product.service.dto.OrderItemDto;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.util.TestUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderApi.class)
@Import(MethodSecurityConfig.class)
class OrderApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final Long ITEM_ID = 10L;
  private static final String ORDERS_URI = "/api/orders";
  private static final String ORDERS_ID_URI = "/api/orders/{id}";
  private static final String ORDERS_ID_ITEMS_URI = "/api/orders/{id}/items";
  private static final String ORDERS_ID_ITEMS_ID_URI =
      "/api/orders/{order_id}/items/{order_item_id}";
  private static final String WRONG_URI = "/api/wrong";

  @MockBean
  private OrderService orderService;

  @Test
  void testCreate() throws Exception {
    var newOrderDto = TestUtils.createNewOrderDto();

    var orderDto = new OrderDto();
    orderDto.setId(ID);
    given(orderService.create(any(NewOrderDto.class))).willReturn(orderDto);

    var result =
        performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(ORDERS_URI + "/" + ID);
  }

  @Test
  void testCreateForbidden() throws Exception {
    var newOrderDto = TestUtils.createNewOrderDto();

    performPost(USER_NOT_EXIST, ORDERS_URI, newOrderDto).andExpect(status().isUnauthorized());

    performPost(USER_WITH_WRONG_ROLE, ORDERS_URI, newOrderDto).andExpect(status().isForbidden());
  }

  @Test
  void testCreateBadRequest() throws Exception {
    // Code
    var newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCode(null);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCode("");
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCode("  ");
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Customer
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCustomer(null);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCustomer("");
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCustomer("  ");
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // PlacedDate
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setPlacedDate(null);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Items
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setItems(null);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setItems(Collections.emptyList());
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Items->Quantity
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).setQuantity(null);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).setQuantity(0);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    // Items->Product
    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).setProduct(null);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.getItems().get(0).getProduct().setId(null);
    performPost(USER, ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    // id
    var orderDto = TestUtils.createUpdatebleOrderDto(null);
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    // code
    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCode(null);
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCode("");
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCode("  ");
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    // Customer
    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCustomer(null);
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCustomer("");
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCustomer("  ");
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    // PlacedDate
    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setPlacedDate(null);
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdate() throws Exception {
    var orderDto = TestUtils.createUpdatebleOrderDto(ID);
    performPatch(USER, ORDERS_ID_URI, ID, orderDto).andExpect(status().isOk());
  }

  @Test
  void testUpdateForbidden() throws Exception {
    UpdatebleOrderDto orderDto = TestUtils.createUpdatebleOrderDto(ID);

    performPatch(USER_NOT_EXIST, ORDERS_ID_URI, ID, orderDto).andExpect(status().isUnauthorized());

    performPatch(USER_WITH_WRONG_ROLE, ORDERS_ID_URI, ID, orderDto)
        .andExpect(status().isForbidden());
  }

  @Test
  void testGetAll() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    OrderDto expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<OrderDto>(List.of(expected)));

    MvcResult result =
        performGet(USER, ORDERS_URI, pageable).andExpect(status().isOk()).andReturn();
    List<OrderDto> list = readValue(result, new TypeReference<List<OrderDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetAllForbidden() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);

    performGet(USER_NOT_EXIST, ORDERS_URI, pageable).andExpect(status().isUnauthorized());

    performGet(USER_WITH_WRONG_ROLE, ORDERS_URI, pageable).andExpect(status().isForbidden());
  }

  @Test
  void testGetOrder() throws Exception {
    OrderDto expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(USER, ORDERS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    OrderDto actual = readValue(result, new TypeReference<OrderDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testGetForbidden() throws Exception {
    performGet(USER_NOT_EXIST, ORDERS_ID_URI, ID).andExpect(status().isUnauthorized());

    performGet(USER_WITH_WRONG_ROLE, ORDERS_ID_URI, ID).andExpect(status().isForbidden());
  }

  @Test
  void testDeleteOrder() throws Exception {
    performDelete(USER, ORDERS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testDeleteForbidden() throws Exception {
    performDelete(USER_NOT_EXIST, ORDERS_ID_URI, ID).andExpect(status().isUnauthorized());

    performDelete(USER_WITH_WRONG_ROLE, ORDERS_ID_URI, ID).andExpect(status().isForbidden());
  }

  @Test
  void testAddItem() throws Exception {
    NewOrderItemDto newOrderItemDto = TestUtils.createNewOrderItemDto();
    OrderItemDto orderItemDto = new OrderItemDto();
    orderItemDto.setId(ITEM_ID);
    given(orderService.addOrderItem(eq(ID), any(NewOrderItemDto.class))).willReturn(orderItemDto);
    MvcResult result = performPut(USER, ORDERS_ID_ITEMS_URI, ID, newOrderItemDto)
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(ORDERS_URI + "/" + ID + "/items/" + ITEM_ID);
  }

  @Test
  void testAddItemForbidden() throws Exception {
    NewOrderItemDto newOrderItemDto = TestUtils.createNewOrderItemDto();

    performPut(USER_NOT_EXIST, ORDERS_ID_ITEMS_URI, ID, newOrderItemDto)
        .andExpect(status().isUnauthorized());

    performPut(USER_WITH_WRONG_ROLE, ORDERS_ID_ITEMS_URI, ID, newOrderItemDto)
        .andExpect(status().isForbidden());
  }

  @Test
  void testAddItemBadRequest() throws Exception {
    // Items->Quantity
    NewOrderItemDto orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setQuantity(null);
    performPut(USER, ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setQuantity(0);
    performPut(USER, ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    // Items->Product
    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setProduct(null);
    performPut(USER, ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.getProduct().setId(null);
    performPut(USER, ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteItem() throws Exception {
    performDelete(USER, ORDERS_ID_ITEMS_ID_URI, ID, ITEM_ID).andExpect(status().isNoContent());
  }

  @Test
  void testDeleteItemForbidden() throws Exception {
    performDelete(USER_NOT_EXIST, ORDERS_ID_ITEMS_ID_URI, ID, ITEM_ID)
        .andExpect(status().isUnauthorized());

    performDelete(USER_WITH_WRONG_ROLE, ORDERS_ID_ITEMS_ID_URI, ID, ITEM_ID)
        .andExpect(status().isForbidden());
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(USER, WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
