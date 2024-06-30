package it.francescofiora.product.api.web.api.impl;

import it.francescofiora.product.api.service.OrderService;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.api.web.api.OrderApi;
import it.francescofiora.product.api.web.errors.BadRequestAlertException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller implementation for managing Order.
 */
@RestController
public class OrderApiImpl extends AbstractApi implements OrderApi {

  private static final String ENTITY_NAME = "OrderDto";
  private static final String ENTITY_ORDER_ITEM = "OrderItemDto";

  private final OrderService orderService;

  public OrderApiImpl(OrderService orderService) {
    super(ENTITY_NAME);
    this.orderService = orderService;
  }

  @Override
  public ResponseEntity<Void> createOrder(NewOrderDto orderDto) {
    var result = orderService.create(orderDto);
    return postResponse("/api/v1/orders/" + result.getId(), result.getId());
  }

  @Override
  public ResponseEntity<Void> patchOrder(UpdatebleOrderDto orderDto, Long id) {
    if (!id.equals(orderDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(orderDto.getId()),
          "Invalid id");
    }
    orderService.patch(orderDto);
    return patchResponse(id);
  }

  @Override
  public ResponseEntity<List<OrderDto>> findOrders(
      String code, String customer, OrderStatus status, Pageable pageable) {
    return getResponse(orderService.findAll(code, customer, status, pageable));
  }

  @Override
  public ResponseEntity<OrderDto> getOrderById(Long id) {
    return getResponse(orderService.findOne(id), id);
  }

  @Override
  public ResponseEntity<Void> deleteOrderById(Long id) {
    orderService.delete(id);
    return deleteResponse(id);
  }

  @Override
  public ResponseEntity<Void> addOrderItem(Long id, NewOrderItemDto orderItemDto) {
    var result = orderService.addOrderItem(id, orderItemDto);
    return postResponse(ENTITY_ORDER_ITEM, "/api/v1/orders/" + id + "/items/" + result.getId(),
        result.getId());
  }

  @Override
  public ResponseEntity<Void> deleteOrderItemById(Long orderId, Long orderItemId) {
    orderService.deleteOrderItem(orderId, orderItemId);
    return deleteResponse(ENTITY_ORDER_ITEM, orderItemId);
  }
}
