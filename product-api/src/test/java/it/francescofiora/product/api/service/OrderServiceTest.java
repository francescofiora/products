package it.francescofiora.product.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class OrderServiceTest {

  private static final Long ID = 1L;
  private static final Long ITEM_ID = 2L;
  private static final Long ID_PRODUCT = 10L;

  @Test
  void testCreate() {
    var order = new Order();
    var orderMapper = mock(OrderMapper.class);
    when(orderMapper.toEntity(any(NewOrderDto.class))).thenReturn(order);

    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    var expected = new OrderDto();
    expected.setId(ID);
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

    var orderDto = new NewOrderDto();
    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        mock(OrderItemRepository.class), orderMapper, mock(OrderItemMapper.class));
    var actual = orderService.create(orderDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testPatchNotFound() {
    var orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    var orderRepository = mock(OrderRepository.class);
    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        mock(OrderItemRepository.class), mock(OrderMapper.class), mock(OrderItemMapper.class));
    assertThrows(NotFoundAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  void testPatchNotUpdateble() {
    var order = new Order();
    order.setId(ID);
    order.setStatus(OrderStatus.COMPLETED);
    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        mock(OrderItemRepository.class), mock(OrderMapper.class), mock(OrderItemMapper.class));
    assertThrows(BadRequestAlertException.class, () -> orderService.patch(orderDto));
  }

  @Test
  void testPatch() {
    var order = TestUtils.createPendingOrder(ID);
    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderDto = new UpdatebleOrderDto();
    orderDto.setId(ID);
    var orderMapper = mock(OrderMapper.class);
    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        mock(OrderItemRepository.class), orderMapper, mock(OrderItemMapper.class));
    orderService.patch(orderDto);
    verify(orderMapper).updateEntityFromDto(orderDto, order);
    verify(orderRepository).save(any(Order.class));
  }

  @Test
  void testFindAll() {
    var order = new Order();
    order.setId(ID);
    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.findAll(ArgumentMatchers.<Example<Order>>any(), any(Pageable.class)))
        .thenReturn(new PageImpl<Order>(List.of(order)));

    var expected = new OrderDto();
    var orderMapper = mock(OrderMapper.class);
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

    var pageable = PageRequest.of(1, 1);
    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        mock(OrderItemRepository.class), orderMapper, mock(OrderItemMapper.class));
    var page = orderService.findAll(null, null, null, pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var orderService =
        new OrderServiceImpl(mock(OrderRepository.class), mock(ProductRepository.class),
            mock(OrderItemRepository.class), mock(OrderMapper.class), mock(OrderItemMapper.class));
    var orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var order = new Order();
    order.setId(ID);
    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));

    var expected = new OrderDto();
    var orderMapper = mock(OrderMapper.class);
    when(orderMapper.toDto(any(Order.class))).thenReturn(expected);

    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        mock(OrderItemRepository.class), orderMapper, mock(OrderItemMapper.class));
    var orderOpt = orderService.findOne(ID);
    assertThat(orderOpt).isPresent();
    var actual = orderOpt.get();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var order = TestUtils.createOrder(ID);
    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        mock(OrderItemRepository.class), mock(OrderMapper.class), mock(OrderItemMapper.class));
    orderService.delete(ID);
  }

  @Test
  void testAddOrderItem() {
    var order = TestUtils.createPendingOrder(ID);
    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderItem = new OrderItem();
    orderItem.setId(ID);
    orderItem.setProduct(new Product());
    orderItem.getProduct().setId(ID_PRODUCT);
    orderItem.setQuantity(10);
    var orderItemMapper = mock(OrderItemMapper.class);
    when(orderItemMapper.toEntity(any(NewOrderItemDto.class))).thenReturn(orderItem);

    var product = new Product();
    product.setPrice(BigDecimal.TEN);
    var productRepository = mock(ProductRepository.class);
    when(productRepository.findById(eq(ID_PRODUCT))).thenReturn(Optional.of(product));

    var orderItemRepository = mock(OrderItemRepository.class);
    when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

    var expected = new OrderItemDto();
    expected.setId(ITEM_ID);
    when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(expected);

    var orderItemDto = new NewOrderItemDto();
    var orderService = new OrderServiceImpl(orderRepository, productRepository, orderItemRepository,
        mock(OrderMapper.class), orderItemMapper);
    var actual = orderService.addOrderItem(ID, orderItemDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDeleteOrderItem() {
    var order = TestUtils.createPendingOrder(ID);
    var orderRepository = mock(OrderRepository.class);
    when(orderRepository.findById(eq(ID))).thenReturn(Optional.of(order));

    var orderItemRepository = mock(OrderItemRepository.class);
    var orderService = new OrderServiceImpl(orderRepository, mock(ProductRepository.class),
        orderItemRepository, mock(OrderMapper.class), mock(OrderItemMapper.class));
    orderService.deleteOrderItem(ID, ITEM_ID);
    verify(orderItemRepository).deleteById(ITEM_ID);
  }
}
