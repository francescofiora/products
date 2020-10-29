package it.francescofiora.product.service.impl;

import it.francescofiora.product.domain.Order;
import it.francescofiora.product.domain.OrderItem;
import it.francescofiora.product.domain.Product;
import it.francescofiora.product.domain.enumeration.OrderStatus;
import it.francescofiora.product.repository.OrderItemRepository;
import it.francescofiora.product.repository.OrderRepository;
import it.francescofiora.product.repository.ProductRepository;
import it.francescofiora.product.service.OrderService;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.OrderDto;
import it.francescofiora.product.service.dto.OrderItemDto;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.service.mapper.NewOrderItemMapper;
import it.francescofiora.product.service.mapper.NewOrderMapper;
import it.francescofiora.product.service.mapper.OrderItemMapper;
import it.francescofiora.product.service.mapper.OrderMapper;
import it.francescofiora.product.service.mapper.UpdatebleOrderMapper;
import it.francescofiora.product.web.errors.BadRequestAlertException;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

  private static final String ENTITY_NAME = "order";

  private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

  private final OrderRepository orderRepository;

  private final OrderItemRepository orderItemRepository;

  private final ProductRepository productRepository;

  private final OrderMapper orderMapper;

  private final OrderItemMapper orderItemMapper;

  private final NewOrderItemMapper newOrderItemMapper;

  private final NewOrderMapper newOrderMapper;

  private final UpdatebleOrderMapper updatebleOrderMapper;

  /**
   * constructor.
   * 
   * @param orderRepository OrderRepository
   * @param productRepository ProductRepository
   * @param orderItemRepository OrderItemRepository
   * @param orderMapper OrderMapper
   * @param newOrderMapper NewOrderMapper
   * @param orderItemMapper OrderItemMapper
   * @param newOrderItemMapper NewOrderItemMapper
   * @param updatebleOrderMapper UpdatebleOrderMapper
   */
  public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
      OrderItemRepository orderItemRepository, OrderMapper orderMapper,
      NewOrderMapper newOrderMapper, OrderItemMapper orderItemMapper,
      NewOrderItemMapper newOrderItemMapper, UpdatebleOrderMapper updatebleOrderMapper) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
    this.newOrderMapper = newOrderMapper;
    this.productRepository = productRepository;
    this.orderItemRepository = orderItemRepository;
    this.updatebleOrderMapper = updatebleOrderMapper;
    this.orderItemMapper = orderItemMapper;
    this.newOrderItemMapper = newOrderItemMapper;
  }

  @Override
  public OrderDto create(NewOrderDto orderDto) {
    log.debug("Request to create a new Order : {}", orderDto);
    Order order = newOrderMapper.toEntity(orderDto);
    order.setStatus(OrderStatus.PENDING);
    order = orderRepository.save(order);
    for (OrderItem item : order.getOrderItems()) {
      item.setOrder(order);
      setTotalPrice(item);
      orderItemRepository.save(item);
    }
    return orderMapper.toDto(order);
  }

  private void setTotalPrice(OrderItem item) {
    Optional<Product> productOpt = productRepository.findById(item.getProduct().getId());
    if (!productOpt.isPresent()) {
      throw new NotFoundAlertException(ProductServiceImpl.ENTITY_NAME,
          String.valueOf(item.getProduct().getId()));
    }
    item.setProduct(productOpt.get());
    item.setTotalPrice(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
  }

  @Override
  public OrderItemDto addOrderItem(Long orderId, NewOrderItemDto orderItemDto) {
    log.debug("Request to create a new item {} to the Order {}", orderItemDto, orderId);
    Order order = findOrderById(orderId);
    checkOrderUpdateble(order);
    OrderItem orderItem = newOrderItemMapper.toEntity(orderItemDto);
    orderItem.setOrder(order);
    setTotalPrice(orderItem);
    orderItem = orderItemRepository.save(orderItem);
    return orderItemMapper.toDto(orderItem);
  }

  @Override
  public void patch(UpdatebleOrderDto orderDto) {
    log.debug("Request to patch Order : {}", orderDto);
    Order order = findOrderById(orderDto.getId());
    checkOrderUpdateble(order);
    updatebleOrderMapper.updateEntityFromDto(orderDto, order);
    orderRepository.save(order);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<OrderDto> findAll(Pageable pageable) {
    log.debug("Request to get all Orders");
    return orderRepository.findAll(pageable).map(orderMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<OrderDto> findOne(Long id) {
    log.debug("Request to get Order : {}", id);
    return orderRepository.findById(id).map(orderMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Order : {}", id);
    orderRepository.deleteById(id);
  }

  @Override
  public void deleteOrderItem(Long orderId, Long orderItemId) {
    log.debug("Request to delete the item {} to the Order {}", orderItemId, orderId);
    checkOrderUpdateble(findOrderById(orderId));
    orderItemRepository.deleteById(orderItemId);
  }

  private void checkOrderUpdateble(Order order) {
    if (!OrderStatus.PENDING.equals(order.getStatus())) {
      throw new BadRequestAlertException(ENTITY_NAME, "ORDER_NOT_UPDATEBLE");
    }
  }

  private Order findOrderById(Long id) {
    Optional<Order> optOrder = orderRepository.findById(id);
    if (!optOrder.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME);
    }
    return optOrder.get();
  }

}
