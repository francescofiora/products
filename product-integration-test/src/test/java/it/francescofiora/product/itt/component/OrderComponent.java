package it.francescofiora.product.itt.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.francescofiora.product.client.ProductApiService;
import it.francescofiora.product.itt.context.OrderContext;
import it.francescofiora.product.itt.context.ProductContext;
import it.francescofiora.product.itt.util.TestProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Order Component.
 */
@Component
@RequiredArgsConstructor
public class OrderComponent extends AbstractComponent {

  private final ProductContext productContext;
  private final OrderContext orderContext;
  private final ProductApiService productApiService;

  public void createNewOrderDto(String code, String customer) {
    orderContext.setNewOrderDto(TestProductUtils.createNewOrderDto(code, customer));
    orderContext.getNewOrderDto().getItems().add(orderContext.getNewOrderItemDto());
  }

  /**
   * Create NewOrderItemDto.
   *
   * @param quantity the quantity of the product
   */
  public void createNewOrderItemDto(String quantity) {
    var resultPr = productApiService.createProduct(productContext.getNewProductDto());
    productContext.setProductId(validateResponseAndGetId(resultPr));
    orderContext.setNewOrderItemDto(TestProductUtils.createNewOrderItemDto(
        productContext.getProductId(), quantity));
  }

  public void createOrder() {
    var result = productApiService.createOrder(orderContext.getNewOrderDto());
    orderContext.setOrderId(validateResponseAndGetId(result));
  }

  /**
   * Create new Item in the order.
   */
  public void createItem() {
    var result = productApiService
        .addOrderItem(orderContext.getOrderId(), orderContext.getNewOrderItemDto());
    orderContext.setItemId(validateResponseAndGetId(result));
  }

  /**
   * Fetch Order.
   */
  public void fetchOrder() {
    var result = productApiService.getOrderById(orderContext.getOrderId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    orderContext.setOrderDto(result.getBody());
  }

  /**
   * Update Order.
   *
   * @param code the code
   * @param customer the customer
   */
  public void updateOrder(String code, String customer) {
    orderContext.setUpdatebleOrderDto(
        TestProductUtils.createUpdatebleOrderDto(orderContext.getOrderId(), code, customer));
    var result = productApiService.patchOrder(orderContext.getUpdatebleOrderDto(),
        orderContext.getOrderId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  public void deleteOrder() {
    var result = productApiService.deleteOrderById(orderContext.getOrderId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Delete Item.
   */
  public void deleteItem() {
    var result = productApiService
        .deleteOrderItemById(orderContext.getOrderId(), orderContext.getItemId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Fetch all Orders.
   */
  public void fetchAllOrders() {
    var result = productApiService.findOrders(null, null, null, Pageable.unpaged());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotEmpty();
    orderContext.setOrders(result.getBody());
  }

  /**
   * Compare OrderDto with NewOrderDto.
   */
  public void compareOrderWithNewOrder() {
    var order = orderContext.getOrderDto();
    assertNotNull(order);
    var newOrderDto = orderContext.getNewOrderDto();
    assertNotNull(newOrderDto);
    assertThat(order.getCode()).isEqualTo(newOrderDto.getCode());
    assertThat(order.getCustomer()).isEqualTo(newOrderDto.getCustomer());
  }

  /**
   * Compare UpdatebleOrderDto into Orders.
   */
  public void compareUpdatebleOrderIntoOrders() {
    var orderId = orderContext.getOrderId();
    var opt = orderContext.getOrders().stream()
        .filter(order -> orderId.equals(order.getId())).findAny();
    assertThat(opt).isNotEmpty();
    var upOrderDto = orderContext.getUpdatebleOrderDto();
    assertThat(opt.get().getCode()).isEqualTo(upOrderDto.getCode());
    assertThat(opt.get().getCustomer()).isEqualTo(upOrderDto.getCustomer());
  }

  /**
   * Check Order not Exist.
   */
  public void checkOrderNotExist() {
    var resultOr = productApiService.findOrders(null, null, null, Pageable.unpaged());
    assertThat(resultOr.getBody()).isNotNull();
    orderContext.setOrders(resultOr.getBody());
    var orderId = orderContext.getOrderId();
    var opt = orderContext.getOrders().stream()
        .filter(order -> orderId.equals(order.getId())).findAny();
    assertThat(opt).isEmpty();
  }

  public void checkSizeItems(int size) {
    assertThat(orderContext.getOrderDto().getItems()).hasSize(size);
  }
}
