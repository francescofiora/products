package it.francescofiora.product.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderApi.class)
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
  void testCreateOrder() throws Exception {
    NewOrderDto newOrderDto = TestUtils.createNewOrderDto();

    OrderDto orderDto = new OrderDto();
    orderDto.setId(ID);
    given(orderService.create(any(NewOrderDto.class))).willReturn(orderDto);

    MvcResult result =
        performPost(ORDERS_URI, newOrderDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(ORDERS_URI + "/" + ID);
  }

  @Test
  void testCreateOrderBadRequest() throws Exception {
    // Code
    NewOrderDto newOrderDto = TestUtils.createNewOrderDto();
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

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCustomer("");
    performPost(ORDERS_URI, newOrderDto).andExpect(status().isBadRequest());

    newOrderDto = TestUtils.createNewOrderDto();
    newOrderDto.setCustomer("  ");
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
  void testUpdateOrderBadRequest() throws Exception {
    // id
    UpdatebleOrderDto orderDto = TestUtils.createUpdatebleOrderDto(null);
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

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCustomer("");
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setCustomer("  ");
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());

    // PlacedDate
    orderDto = TestUtils.createUpdatebleOrderDto(ID);
    orderDto.setPlacedDate(null);
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateOrder() throws Exception {
    UpdatebleOrderDto orderDto = TestUtils.createUpdatebleOrderDto(ID);
    performPatch(ORDERS_ID_URI, ID, orderDto).andExpect(status().isOk());
  }

  @Test
  void testGetAllOrders() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    OrderDto expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<OrderDto>(Collections.singletonList(expected)));

    MvcResult result = performGet(ORDERS_URI, pageable).andExpect(status().isOk()).andReturn();
    List<OrderDto> list = readValue(result, new TypeReference<List<OrderDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetOrder() throws Exception {
    OrderDto expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(ORDERS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    OrderDto actual = readValue(result, new TypeReference<OrderDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testDeleteOrder() throws Exception {
    performDelete(ORDERS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testAddOrderItem() throws Exception {
    NewOrderItemDto newOrderItemDto = TestUtils.createNewOrderItemDto();
    OrderItemDto orderItemDto = new OrderItemDto();
    orderItemDto.setId(ITEM_ID);
    given(orderService.addOrderItem(eq(ID), any(NewOrderItemDto.class))).willReturn(orderItemDto);
    MvcResult result = performPut(ORDERS_ID_ITEMS_URI, ID, newOrderItemDto)
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(ORDERS_URI + "/" + ID + "/items/" + ITEM_ID);
  }

  @Test
  void testAddOrderItemBadRequest() throws Exception {
    // Items->Quantity
    NewOrderItemDto orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setQuantity(null);
    performPut(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setQuantity(0);
    performPut(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    // Items->Product
    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.setProduct(null);
    performPut(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());

    orderItemDto = TestUtils.createNewOrderItemDto();
    orderItemDto.getProduct().setId(null);
    performPut(ORDERS_ID_ITEMS_URI, ID, orderItemDto).andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteOrderItem() throws Exception {
    performDelete(ORDERS_ID_ITEMS_ID_URI, ID, ITEM_ID).andExpect(status().isNoContent())
        .andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
