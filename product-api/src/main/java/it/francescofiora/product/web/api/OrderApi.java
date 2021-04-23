package it.francescofiora.product.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.service.OrderService;
import it.francescofiora.product.service.dto.NewOrderDto;
import it.francescofiora.product.service.dto.NewOrderItemDto;
import it.francescofiora.product.service.dto.OrderDto;
import it.francescofiora.product.service.dto.OrderItemDto;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.web.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link it.francescofiora.product.domain.Order}.
 */
@RestController
@RequestMapping("/api")
public class OrderApi extends AbstractApi {

  private final Logger log = LoggerFactory.getLogger(OrderApi.class);

  private static final String ENTITY_NAME = "OrderDto";

  private static final String ENTITY_ORDER_ITEM = "OrderItemDto";

  private final OrderService orderService;

  public OrderApi(OrderService orderService) {
    super(ENTITY_NAME);
    this.orderService = orderService;
  }

  /**
   * {@code POST  /orders} : Create a new order.
   *
   * @param orderDto the orderDto to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}, or with status
   *         {@code 400 (Bad Request)} if the order has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(summary = "Add new Order", description = "Add a new Order to the system",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Order created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "409", description = "An existing Order already exists")})
  @PostMapping("/orders")
  public ResponseEntity<Void> createOrder(
      @Parameter(description = "Add new Order") @Valid @RequestBody NewOrderDto orderDto)
      throws URISyntaxException {
    log.debug("REST request to create a new Order : {}", orderDto);
    OrderDto result = orderService.create(orderDto);
    return postResponse("/api/orders/" + result.getId(), result.getId());
  }

  /**
   * {@code PATCH  /orders} : Patches an existing order.
   *
   * @param orderDto the orderDto to patch.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}, or with status
   *         {@code 400 (Bad Request)} if the orderDto is not valid, or with status
   *         {@code 500 (Internal Server Error)} if the orderDto couldn't be patched.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(summary = "Path Order", description = "Patch an Order to the system", tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Order patched"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PatchMapping("/orders/{id}")
  public ResponseEntity<Void> patchOrder(
      @Parameter(description = "Order to update") @Valid @RequestBody UpdatebleOrderDto orderDto,
      @Parameter(description = "The id of the order to patch", required = true,
          example = "1") @PathVariable("id") Long id) {
    log.debug("REST request to patch Order : {}", orderDto);
    if (!id.equals(orderDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(orderDto.getId()),
          "Invalid id");
    }
    orderService.patch(orderDto);
    return patchResponse(id);
  }

  /**
   * {@code GET  /orders} : get all the orders.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
   */
  @Operation(summary = "Searches Orders",
      description = "By passing in the appropriate options, "
          + "you can search for available categories in the system",
      tags = {"order"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/orders")
  public ResponseEntity<List<OrderDto>> getAllOrders(Pageable pageable) {
    log.debug("REST request to get a page of Orders");
    return getResponse(orderService.findAll(pageable));
  }

  /**
   * {@code GET  /orders/:id} : get the "id" order.
   *
   * @param id the id of the orderDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDto, or
   *         with status {@code 404 (Not Found)}.
   */
  @Operation(summary = "Searches Order by 'id'", description = "Searches Order by 'id'",
      tags = {"order"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = OrderDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/orders/{id}")
  public ResponseEntity<OrderDto> getOrder(
      @Parameter(description = "Id of the Order to get") @PathVariable Long id) {
    log.debug("REST request to get Order : {}", id);
    return getResponse(orderService.findOne(id), id);
  }

  /**
   * {@code DELETE  /orders/:id} : delete the "id" order.
   *
   * @param id the id of the orderDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(summary = "Delete Order", description = "Delete an Order to the system",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Order deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/orders/{id}")
  public ResponseEntity<Void> deleteOrder(
      @Parameter(description = "The id of the Order to delete") @PathVariable Long id) {
    log.debug("REST request to delete Order : {}", id);
    orderService.delete(id);
    return deleteResponse(id);
  }

  /**
   * Add OrderItem.
   * 
   * @param id Order id
   * @param orderItemDto NewOrderItemDto
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}, or with status
   *         {@code 400 (Bad Request)} if the order has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(summary = "Add OrderItem", description = "Add a new item to an Order",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OrderItem added"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/orders/{id}/items")
  public ResponseEntity<Void> addOrderItem(
      @Parameter(description = "Order id") @PathVariable Long id,
      @Parameter(description = "Item to add") @Valid @RequestBody NewOrderItemDto orderItemDto)
      throws URISyntaxException {
    log.debug("REST request to add a new Item {} to the order {}", orderItemDto, id);
    OrderItemDto result = orderService.addOrderItem(id, orderItemDto);
    return postResponse(ENTITY_ORDER_ITEM, "/api/orders/" + id + "/items/" + result.getId(),
        result.getId());
  }

  /**
   * {@code DELETE  /orders/:id/items/:id} : delete the "id" order.
   *
   * @param orderId the id of the order.
   * @param orderItemId the id of the orderItemDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(summary = "Delete item of a Order", description = "Delete an item of a Order",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "OrderItem deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/orders/{order_id}/items/{order_item_id}")
  public ResponseEntity<Void> deleteOrderItem(
      @Parameter(description = "Id of the Order") @PathVariable(name = "order_id") Long orderId,
      @Parameter(description = "Id of the Item to delete") @PathVariable(
          name = "order_item_id") Long orderItemId) {
    log.debug("REST request to delete the Item {} to the order {}", orderItemId, orderId);
    orderService.deleteOrderItem(orderId, orderItemId);
    return deleteResponse(ENTITY_ORDER_ITEM, orderItemId);
  }
}
