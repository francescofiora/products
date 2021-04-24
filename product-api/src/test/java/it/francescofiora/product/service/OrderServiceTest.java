package it.francescofiora.product.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
import it.francescofiora.product.service.mapper.OrderItemMapper;
import it.francescofiora.product.service.mapper.OrderMapper;
import it.francescofiora.product.util.TestUtils;
import it.francescofiora.product.web.errors.BadRequestAlertException;
import it.francescofiora.product.web.errors.NotFoundAlertException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

  private static final Long ID = 1L;

  private static final Long ITEM_ID = 2L;

  private static final Long ID_PRODUCT = 10L;

  @MockBean
  private OrderService orderService;

  @MockBean
  private ProductRepository productRepository;

  @MockBean
  private OrderItemMapper orderItemMapper;

  @MockBean
  private OrderMapper orderMapper;

  @MockBean
  private OrderItemRepository orderItemRepository;

  @MockBean
  private OrderRepository orderRepository;

  @BeforeEach
  public void setUp() {
    orderService = new OrderServiceImpl(orderRepository, productRepository, orderItemRepository,
        orderMapper, orderItemMapper);
  }

  @Test
  void testCreate() throws Exception {
    Order order = new Order();
    when(orderMapper.toEntity(any(NewOrderDto.class))).thenReturn(order);

    when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderDto expected = new OrderDto();
    expected.setId(ID);
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

    NewOrderDto orderDto = new NewOrderDto();
    OrderDto actual = orderService.create(orderDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testPatchNotFound() throws Exception {
    UpdatebleOrderDto orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    assertThrows(NotFoundAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  void testPatchNotUpdateble() throws Exception {
    Order order = new Order();
    order.setId(ID);
    order.setStatus(OrderStatus.COMPLETED);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    UpdatebleOrderDto orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    assertThrows(BadRequestAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  void testPatch() throws Exception {
    Order order = TestUtils.createPendingOrder(ID);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    UpdatebleOrderDto orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    orderService.patch(orderDto);
  }

  @Test
  void testFindAll() throws Exception {
    Order order = new Order();
    order.setId(ID);
    when(orderRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Order>(singletonList(order)));
    OrderDto expected = new OrderDto();
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<OrderDto> page = orderService.findAll(pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    Optional<OrderDto> orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    Order order = new Order();
    order.setId(ID);
    when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));
    OrderDto expected = new OrderDto();
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

    Optional<OrderDto> orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isPresent();
    OrderDto actual = orderOpt.get();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    orderService.delete(ID);
  }

  @Test
  void testAddOrderItem() throws Exception {
    Order order = TestUtils.createPendingOrder(ID);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    OrderItem orderItem = new OrderItem();
    orderItem.setId(ID);
    orderItem.setProduct(new Product());
    orderItem.getProduct().setId(ID_PRODUCT);
    orderItem.setQuantity(10);
    when(orderItemMapper.toEntity(any(NewOrderItemDto.class))).thenReturn(orderItem);

    Product product = new Product();
    product.setPrice(BigDecimal.TEN);
    when(productRepository.findById(eq(ID_PRODUCT))).thenReturn(Optional.of(product));

    when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

    OrderItemDto expected = new OrderItemDto();
    expected.setId(ITEM_ID);
    when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(expected);

    NewOrderItemDto orderItemDto = new NewOrderItemDto();
    OrderItemDto actual = orderService.addOrderItem(ID, orderItemDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDeleteOrderItem() throws Exception {
    Order order = TestUtils.createPendingOrder(ID);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));
    orderService.deleteOrderItem(ID, ITEM_ID);
  }
}
