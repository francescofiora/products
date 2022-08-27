package it.francescofiora.product.client;

import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Order Client Service.
 */
public interface OrderClientService {

  /**
   * Create a new order.
   *
   * @param orderDto the entity to save.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> create(NewOrderDto orderDto);

  /**
   * Patch a order.
   *
   * @param orderDto the entity to patch.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> patch(UpdatebleOrderDto orderDto);

  /**
   * Create a new OrderItem.
   *
   * @param orderId Long
   * @param orderItemDto NewOrderItemDto
   * @return the result.
   */
  Mono<ResponseEntity<Void>> addOrderItem(Long orderId, NewOrderItemDto orderItemDto);

  /**
   * Get all the orders.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Flux<OrderDto> findAll(Pageable pageable);

  /**
   * Get the "id" order.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Mono<ResponseEntity<OrderDto>> findOne(Long id);

  /**
   * Delete the "id" order.
   *
   * @param id the id of the entity.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> delete(Long id);

  /**
   * Delete the "id" orderItem to the order.
   *
   * @param orderId the id of the order.
   * @param orderItemId the id of the entity.
   * @return the result.
   */
  Mono<ResponseEntity<Void>> deleteOrderItem(Long orderId, Long orderItemId);
}
