package it.francescofiora.product.itt.context;

import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Order Context.
 */
@Component
@Getter
@Setter
public class OrderContext {

  private NewOrderDto newOrderDto;
  private NewOrderItemDto newOrderItemDto;
  private UpdatebleOrderDto updatebleOrderDto;
  private OrderDto orderDto;
  private List<OrderDto> orders;
  private Long orderId;
  private Long itemId;
}
