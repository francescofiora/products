package it.francescofiora.product.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.francescofiora.product.api.domain.Order;
import it.francescofiora.product.api.domain.OrderItem;
import it.francescofiora.product.api.domain.Product;
import it.francescofiora.product.api.repository.OrderItemRepository;
import it.francescofiora.product.api.repository.OrderRepository;
import it.francescofiora.product.api.repository.ProductRepository;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.OrderItemDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.api.service.impl.OrderServiceImpl;
import it.francescofiora.product.api.service.mapper.OrderItemMapper;
import it.francescofiora.product.api.service.mapper.OrderMapper;
import it.francescofiora.product.api.util.TestUtils;
import it.francescofiora.product.api.web.errors.BadRequestAlertException;
import it.francescofiora.product.api.web.errors.NotFoundAlertException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    var order = new Order();
    when(orderMapper.toEntity(any(NewOrderDto.class))).thenReturn(order);

    when(orderRepository.save(any(Order.class))).thenReturn(order);

    var expected = new OrderDto();
    expected.setId(ID);
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

    var orderDto = new NewOrderDto();
    var actual = orderService.create(orderDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testPatchNotFound() throws Exception {
    var orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    assertThrows(NotFoundAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  void testPatchNotUpdateble() throws Exception {
    var order = new Order();
    order.setId(ID);
    order.setStatus(OrderStatus.COMPLETED);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    assertThrows(BadRequestAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  void testPatch() throws Exception {
    var order = TestUtils.createPendingOrder(ID);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    orderService.patch(orderDto);
  }

  @Test
  void testFindAll() throws Exception {
    var order = new Order();
    order.setId(ID);
    when(orderRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Order>(List.of(order)));
    var expected = new OrderDto();
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = orderService.findAll(pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var order = new Order();
    order.setId(ID);
    when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));
    var expected = new OrderDto();
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

    var orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isPresent();
    var actual = orderOpt.get();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    var order = TestUtils.createOrder(ID);

    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));
    orderService.delete(ID);
  }

  @Test
  void testAddOrderItem() throws Exception {
    var order = TestUtils.createPendingOrder(ID);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderItem = new OrderItem();
    orderItem.setId(ID);
    orderItem.setProduct(new Product());
    orderItem.getProduct().setId(ID_PRODUCT);
    orderItem.setQuantity(10);
    when(orderItemMapper.toEntity(any(NewOrderItemDto.class))).thenReturn(orderItem);

    var product = new Product();
    product.setPrice(BigDecimal.TEN);
    when(productRepository.findById(eq(ID_PRODUCT))).thenReturn(Optional.of(product));

    when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

    var expected = new OrderItemDto();
    expected.setId(ITEM_ID);
    when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(expected);

    var orderItemDto = new NewOrderItemDto();
    var actual = orderService.addOrderItem(ID, orderItemDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDeleteOrderItem() throws Exception {
    var order = TestUtils.createPendingOrder(ID);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));
    orderService.deleteOrderItem(ID, ITEM_ID);
  }
}
