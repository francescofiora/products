package it.francescofiora.product.client.impl;

import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.client.ClientInfo;
import it.francescofiora.product.client.OrderClientService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Order Client Service Impl.
 */
@Component
public class OrderClientServiceImpl extends AbtractClient implements OrderClientService {

  private static final String ORDERS_URI = "/api/v1/orders";
  private static final String ORDERS_ID_URI = "/api/v1/orders/{id}";
  private static final String ORDERS_ITEMS_URI = "/api/v1/orders/{id}/items";
  private static final String ORDERS_ITEMS_ID_URI =
      "/api/v1/orders/{order_id}/items/{order_item_id}";

  /**
   * Constructor.
   *
   * @param clientInfo the ClientInfo
   */
  public OrderClientServiceImpl(ClientInfo clientInfo) {
    super(clientInfo);
  }

  @Override
  public Mono<ResponseEntity<Void>> create(NewOrderDto orderDto) {
    return create(ORDERS_URI, orderDto, NewOrderDto.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> patch(UpdatebleOrderDto orderDto) {
    return patch(ORDERS_ID_URI, orderDto, UpdatebleOrderDto.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> addOrderItem(Long orderId, NewOrderItemDto orderItemDto) {
    return create(ORDERS_ITEMS_URI, orderId, orderItemDto, NewOrderItemDto.class);
  }

  @Override
  public Flux<OrderDto> findAll(Pageable pageable) {
    return findAll(pageable, ORDERS_URI, OrderDto.class);
  }

  @Override
  public Mono<ResponseEntity<OrderDto>> findOne(Long id) {
    return findOne(ORDERS_ID_URI, id, OrderDto.class);
  }

  @Override
  public Mono<ResponseEntity<Void>> delete(Long id) {
    return delete(ORDERS_ID_URI, id);
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteOrderItem(Long orderId, Long orderItemId) {
    return delete(ORDERS_ITEMS_ID_URI, orderId, orderItemId);
  }
}
