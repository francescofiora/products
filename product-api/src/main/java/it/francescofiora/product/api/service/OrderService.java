package it.francescofiora.product.api.service;

import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.OrderItemDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Order.
 */
public interface OrderService {

  /**
   * Create a new order.
   *
   * @param orderDto the entity to save
   * @return the persisted entity
   */
  OrderDto create(NewOrderDto orderDto);

  /**
   * Create a new OrderItem.
   *
   * @param orderId Long
   * @param orderItemDto NewOrderItemDto
   * @return OrderItemDto
   */
  OrderItemDto addOrderItem(Long orderId, NewOrderItemDto orderItemDto);

  /**
   * Patch a order.
   *
   * @param orderDto the entity to patch
   */
  void patch(UpdatebleOrderDto orderDto);

  /**
   * Get all the orders.
   *
   * @param code the code
   * @param customerId the customer id
   * @param status the order status
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<OrderDto> findAll(String code, Long customerId, OrderStatus status, Pageable pageable);

  /**
   * Get the "id" order.
   *
   * @param id the id of the entity
   * @return the entity
   */
  Optional<OrderDto> findOne(Long id);

  /**
   * Delete the "id" order.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * Delete the "id" orderItem to the order.
   *
   * @param orderId the id of the order
   * @param orderItemId the id of the entity
   */
  void deleteOrderItem(Long orderId, Long orderItemId);
}
