package it.francescofiora.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Order;
import it.francescofiora.product.domain.OrderItem;
import it.francescofiora.product.domain.Product;
import it.francescofiora.product.repository.OrderItemRepository;
import it.francescofiora.product.repository.OrderRepository;
import it.francescofiora.product.repository.ProductRepository;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.OrderDto;
import it.francescofiora.product.service.dto.OrderItemDto;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.service.impl.OrderServiceImpl;
import it.francescofiora.product.service.mapper.NewOrderItemMapper;
import it.francescofiora.product.service.mapper.NewOrderMapper;
import it.francescofiora.product.service.mapper.OrderItemMapper;
import it.francescofiora.product.service.mapper.OrderMapper;
import it.francescofiora.product.service.mapper.UpdatebleOrderMapper;
import it.francescofiora.product.web.errors.BadRequestAlertException;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

  private static final Long ID = 1L;

  private static final Long ITEM_ID = 2L;

  private static final Long ID_PRODUCT = 10L;

  @MockBean
  private OrderService orderService;

  @MockBean
  private ProductRepository productRepository;

  @MockBean
  private NewOrderItemMapper newOrderItemMapper;

  @MockBean
  private OrderItemMapper orderItemMapper;

  @MockBean
  private NewOrderMapper newOrderMapper;

  @MockBean
  private OrderMapper orderMapper;

  @MockBean
  private OrderItemRepository orderItemRepository;

  @MockBean
  private UpdatebleOrderMapper updatebleOrderMapper;

  @MockBean
  private OrderRepository orderRepository;

  @BeforeEach
  public void setUp() {
    orderService = new OrderServiceImpl(orderRepository, productRepository, orderItemRepository,
        orderMapper, newOrderMapper, orderItemMapper, newOrderItemMapper, updatebleOrderMapper);
  }

  @Test
  public void testCreate() throws Exception {
    Order order = new Order();
    Mockito.when(newOrderMapper.toEntity(Mockito.any(NewOrderDto.class))).thenReturn(order);

    Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

    OrderDto expected = new OrderDto();
    expected.setId(ID);
    Mockito.when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(expected);

    NewOrderDto orderDto = new NewOrderDto();
    OrderDto actual = orderService.create(orderDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testPatchNotFound() throws Exception {
    UpdatebleOrderDto orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    Assertions.assertThrows(NotFoundAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  public void testPatchNotUpdateble() throws Exception {
    Order order = new Order();
    order.setId(ID);
    order.setStatus(OrderStatus.COMPLETED);
    Mockito.when(orderRepository.findById(Mockito.eq(ID))).thenReturn(Optional.of(order));

    UpdatebleOrderDto orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    Assertions.assertThrows(BadRequestAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  public void testPatch() throws Exception {
    Order order = getPendingOrder();
    Mockito.when(orderRepository.findById(Mockito.eq(ID))).thenReturn(Optional.of(order));

    UpdatebleOrderDto orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    orderService.patch(orderDto);
  }

  @Test
  public void testFindAll() throws Exception {
    Order order = new Order();
    order.setId(ID);
    Mockito.when(orderRepository.findAll(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<Order>(Collections.singletonList(order)));
    OrderDto expected = new OrderDto();
    Mockito.when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<OrderDto> page = orderService.findAll(pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<OrderDto> orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isNotPresent();
  }

  @Test
  public void testFindOne() throws Exception {
    Order order = new Order();
    order.setId(ID);
    Mockito.when(orderRepository.findById(Mockito.eq(order.getId())))
        .thenReturn(Optional.of(order));
    OrderDto expected = new OrderDto();
    Mockito.when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(expected);

    Optional<OrderDto> orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isPresent();
    OrderDto actual = orderOpt.get();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDelete() throws Exception {
    orderService.delete(ID);
  }

  @Test
  public void testAddOrderItem() throws Exception {
    Order order = getPendingOrder();
    Mockito.when(orderRepository.findById(Mockito.eq(ID))).thenReturn(Optional.of(order));

    OrderItem orderItem = new OrderItem();
    orderItem.setId(ID);
    orderItem.setProduct(new Product());
    orderItem.getProduct().setId(ID_PRODUCT);
    orderItem.setQuantity(10);
    Mockito.when(newOrderItemMapper.toEntity(Mockito.any(NewOrderItemDto.class)))
        .thenReturn(orderItem);

    Product product = new Product();
    product.setPrice(BigDecimal.TEN);
    Mockito.when(productRepository.findById(Mockito.eq(ID_PRODUCT)))
        .thenReturn(Optional.of(product));

    Mockito.when(orderItemRepository.save(Mockito.any(OrderItem.class))).thenReturn(orderItem);

    OrderItemDto expected = new OrderItemDto();
    expected.setId(ITEM_ID);
    Mockito.when(orderItemMapper.toDto(Mockito.any(OrderItem.class))).thenReturn(expected);

    NewOrderItemDto orderItemDto = new NewOrderItemDto();
    OrderItemDto actual = orderService.addOrderItem(ID, orderItemDto);
 
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDeleteOrderItem() throws Exception {
    Order order = getPendingOrder();
    Mockito.when(orderRepository.findById(Mockito.eq(order.getId())))
        .thenReturn(Optional.of(order));
    orderService.deleteOrderItem(ID, ITEM_ID);
  }

  private Order getPendingOrder() {
    Order order = new Order();
    order.setId(ID);
    order.setStatus(OrderStatus.PENDING);
    return order;
  }
}
