package it.francescofiora.product.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.service.OrderService;
import it.francescofiora.product.service.dto.BaseOrderDto;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.OrderDto;
import it.francescofiora.product.service.dto.OrderItemDto;
import it.francescofiora.product.service.dto.RefProductDto;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderApi.class)
public class OrderApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final Long ITEM_ID = 10L;
  private static final String ORDERS_URI = "/api/orders";
  private static final String ORDERS_ID_URI = "/api/orders/{id}";
  private static final String ORDERS_ID_ITEMS_URI = "/api/orders/{id}/items";
  private static final String ORDERS_ID_ITEMS_ID_URI =
      "/api/orders/{order_id}/items/{order_item_id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private OrderService orderService;

  @Test
  public void testCreateOrder() throws Exception {
    NewOrderDto newOrderDto = getNewOrderDto();

    OrderDto orderDto = new OrderDto();
    orderDto.setId(ID);
    given(orderService.create(any(NewOrderDto.class))).willReturn(orderDto);
    MvcResult result = mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(ORDERS_URI + "/" + ID);
  }

  @Test
  public void testCreateOrderBadRequest() throws Exception {
    // Code
    NewOrderDto newOrderDto = getNewOrderDto();
    newOrderDto.setCode(null);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    newOrderDto = getNewOrderDto();
    newOrderDto.setCode("");
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    newOrderDto = getNewOrderDto();
    newOrderDto.setCode("  ");
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    // Customer
    newOrderDto = getNewOrderDto();
    newOrderDto.setCustomer(null);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    newOrderDto = getNewOrderDto();
    newOrderDto.setCustomer("");
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    newOrderDto = getNewOrderDto();
    newOrderDto.setCustomer("  ");
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    // PlacedDate
    newOrderDto = getNewOrderDto();
    newOrderDto.setPlacedDate(null);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    // Items
    newOrderDto = getNewOrderDto();
    newOrderDto.setItems(null);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    newOrderDto = getNewOrderDto();
    newOrderDto.setItems(Collections.emptyList());
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    // Items->Quantity
    newOrderDto = getNewOrderDto();
    newOrderDto.getItems().get(0).setQuantity(null);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    newOrderDto = getNewOrderDto();
    newOrderDto.getItems().get(0).setQuantity(0);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    // Items->Product
    newOrderDto = getNewOrderDto();
    newOrderDto.getItems().get(0).setProduct(null);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());

    newOrderDto = getNewOrderDto();
    newOrderDto.getItems().get(0).getProduct().setId(null);
    mvc.perform(post(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newOrderDto))).andExpect(status().isBadRequest());
  }

  private NewOrderDto getNewOrderDto() {
    NewOrderDto newOrderDto = new NewOrderDto();
    fillOrder(newOrderDto);
    newOrderDto.setItems(getItems());
    return newOrderDto;
  }

  @Test
  public void testUpdateOrderBadRequest() throws Exception {
    // id
    UpdatebleOrderDto orderDto = updateOrderDto();
    orderDto.setId(null);
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());

    // code
    orderDto = updateOrderDto();
    orderDto.setCode(null);
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());

    orderDto = updateOrderDto();
    orderDto.setCode("");
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());

    orderDto = updateOrderDto();
    orderDto.setCode("  ");
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());

    // Customer
    orderDto = updateOrderDto();
    orderDto.setCustomer(null);
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());

    orderDto = updateOrderDto();
    orderDto.setCustomer("");
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());

    orderDto = updateOrderDto();
    orderDto.setCustomer("  ");
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());

    // PlacedDate
    orderDto = updateOrderDto();
    orderDto.setPlacedDate(null);
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateOrder() throws Exception {
    UpdatebleOrderDto orderDto = updateOrderDto();
    mvc.perform(patch(ORDERS_ID_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderDto))).andExpect(status().isOk());
  }

  private UpdatebleOrderDto updateOrderDto() {
    UpdatebleOrderDto orderDto = new UpdatebleOrderDto();
    fillOrder(orderDto);
    orderDto.setId(ID);
    return orderDto;
  }

  private List<NewOrderItemDto> getItems() {
    return Collections.singletonList(getNewOrderItemDto());
  }

  private NewOrderItemDto getNewOrderItemDto() {
    NewOrderItemDto orderItemDto = new NewOrderItemDto();
    orderItemDto.setProduct(getProduct());
    orderItemDto.setQuantity(2);
    return orderItemDto;
  }

  private RefProductDto getProduct() {
    RefProductDto productDto = new RefProductDto();
    productDto.setId(ID);
    return productDto;
  }

  private void fillOrder(BaseOrderDto orderDto) {
    orderDto.setCode("CODE");
    orderDto.setCustomer("Customer");
    orderDto.setPlacedDate(Instant.now());
  }

  @Test
  public void testGetAllOrders() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    OrderDto expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<OrderDto>(Collections.singletonList(expected)));

    MvcResult result = mvc.perform(get(new URI(ORDERS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<OrderDto> list = readValue(result, new TypeReference<List<OrderDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetOrder() throws Exception {
    OrderDto expected = new OrderDto();
    expected.setId(ID);
    given(orderService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(ORDERS_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    OrderDto actual = readValue(result, new TypeReference<OrderDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeleteOrder() throws Exception {
    mvc.perform(delete(ORDERS_ID_URI, ID)).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testAddOrderItem() throws Exception {
    NewOrderItemDto newOrderItemDto = getNewOrderItemDto();
    OrderItemDto orderItemDto = new OrderItemDto();
    orderItemDto.setId(ITEM_ID);
    given(orderService.addOrderItem(eq(ID), any(NewOrderItemDto.class))).willReturn(orderItemDto);
    MvcResult result = mvc
        .perform(put(ORDERS_ID_ITEMS_URI, ID).contentType(APPLICATION_JSON)
            .content(writeValueAsString(newOrderItemDto)))
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location"))
        .isEqualTo(ORDERS_URI + "/" + ID + "/items/" + ITEM_ID);
  }

  @Test
  public void testAddOrderItemBadRequest() throws Exception {
    // Items->Quantity
    NewOrderItemDto orderItemDto = getNewOrderItemDto();
    orderItemDto.setQuantity(null);
    mvc.perform(put(ORDERS_ID_ITEMS_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderItemDto))).andExpect(status().isBadRequest());

    orderItemDto = getNewOrderItemDto();
    orderItemDto.setQuantity(0);
    mvc.perform(put(ORDERS_ID_ITEMS_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderItemDto))).andExpect(status().isBadRequest());

    // Items->Product
    orderItemDto = getNewOrderItemDto();
    orderItemDto.setProduct(null);
    mvc.perform(put(ORDERS_ID_ITEMS_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderItemDto))).andExpect(status().isBadRequest());

    orderItemDto = getNewOrderItemDto();
    orderItemDto.getProduct().setId(null);
    mvc.perform(put(ORDERS_ID_ITEMS_URI, ID).contentType(APPLICATION_JSON)
        .content(writeValueAsString(orderItemDto))).andExpect(status().isBadRequest());

  }

  @Test
  public void testDeleteOrderItem() throws Exception {
    mvc.perform(delete(ORDERS_ID_ITEMS_ID_URI, ID, ITEM_ID)).andExpect(status().isNoContent())
        .andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    mvc.perform(get(WRONG_URI)).andExpect(status().isNotFound()).andReturn();
  }
}
