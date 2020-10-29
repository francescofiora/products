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
import it.francescofiora.product.web.util.HeaderUtil;
import it.francescofiora.product.web.util.PaginationUtil;
import it.francescofiora.product.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link it.francescofiora.product.domain.Order}.
 */
@RestController
@RequestMapping("/api")
public class OrderApi {

  private final Logger log = LoggerFactory.getLogger(OrderApi.class);

  private static final String ORDER = "order";

  private static final String ORDER_ITEM = "orderItem";

  private final OrderService orderService;

  public OrderApi(OrderService orderService) {
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
  @Operation(summary = "add new Order", description = "add a new Order to the system",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Order created"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "409", description = "an existing Order already exists")})
  @PostMapping("/orders")
  public ResponseEntity<Void> createOrder(
      @Parameter(description = "add new Order") @Valid @RequestBody NewOrderDto orderDto)
      throws URISyntaxException {
    log.debug("REST request to create a new Order : {}", orderDto);
    OrderDto result = orderService.create(orderDto);
    return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ORDER, result.getId().toString())).build();
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
  @Operation(summary = "path Order", description = "patch an Order to the system", tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Order patched"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @PatchMapping("/orders")
  public ResponseEntity<Void> patchOrder(
      @Parameter(description = "Order to update") @Valid @RequestBody UpdatebleOrderDto orderDto)
      throws URISyntaxException {
    log.debug("REST request to patch Order : {}", orderDto);
    if (orderDto.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ORDER, "idnull");
    }
    orderService.patch(orderDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ORDER, orderDto.getId().toString())).build();
  }

  /**
   * {@code GET  /orders} : get all the orders.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
   */
  @Operation(summary = "searches Orders", description = "By passing in the appropriate options, "
      + "you can search for available categories in the system", tags = {"order"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))),
      @ApiResponse(responseCode = "400", description = "bad input parameter")})
  @GetMapping("/orders")
  public ResponseEntity<List<OrderDto>> getAllOrders(Pageable pageable) {
    log.debug("REST request to get a page of Orders");
    Page<OrderDto> page = orderService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /orders/:id} : get the "id" order.
   *
   * @param id the id of the orderDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDto, or
   *         with status {@code 404 (Not Found)}.
   */
  @Operation(summary = "searches Order by 'id'", description = "searches Order by 'id'",
      tags = {"order"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "search results matching criteria",
          content = @Content(schema = @Schema(implementation = OrderDto.class))),
      @ApiResponse(responseCode = "400", description = "bad input parameter"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @GetMapping("/orders/{id}")
  public ResponseEntity<OrderDto> getOrder(
      @Parameter(description = "Order to get") @PathVariable Long id) {
    log.debug("REST request to get Order : {}", id);
    Optional<OrderDto> orderDto = orderService.findOne(id);
    return ResponseUtil.wrapOrNotFound(ORDER, orderDto);
  }

  /**
   * {@code DELETE  /orders/:id} : delete the "id" order.
   *
   * @param id the id of the orderDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(summary = "delete Order", description = "delete an Order to the system",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Order deleted"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @DeleteMapping("/orders/{id}")
  public ResponseEntity<Void> deleteOrder(
      @Parameter(description = "Order to delete") @PathVariable Long id) {
    log.debug("REST request to delete Order : {}", id);
    orderService.delete(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(ORDER, id.toString())).build();
  }

  /**
   * add OrderItem.
   * 
   * @param id Order id
   * @param orderItemDto NewOrderItemDto
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}, or with status
   *         {@code 400 (Bad Request)} if the order has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(summary = "add OrderItem", description = "add a new item to an Order",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OrderItem added"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @PutMapping("/orders/{id}/items")
  public ResponseEntity<Void> addOrderItem(
      @Parameter(description = "Order id") @PathVariable Long id,
      @Parameter(description = "Item to add") @Valid @RequestBody NewOrderItemDto orderItemDto)
      throws URISyntaxException {
    log.debug("REST request to add a new Item {} to the order {}", orderItemDto, id);
    OrderItemDto result = orderService.addOrderItem(id, orderItemDto);
    return ResponseEntity.created(new URI("/api/orders/" + id + "/items/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ORDER_ITEM, result.getId().toString()))
        .build();
  }

  /**
   * {@code DELETE  /orders/:id/items/:id} : delete the "id" order.
   *
   * @param orderId the id of the order.
   * @param orderItemId the id of the orderItemDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(summary = "delete item of a Order", description = "delete an item of a Order",
      tags = {"order"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "OrderItem deleted"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @DeleteMapping("/orders/{order_id}/items/{order_item_id}")
  public ResponseEntity<Void> deleteOrderItem(
      @Parameter(description = "Id of the Order") @PathVariable(name = "order_id") Long orderId,
      @Parameter(description = "Id of the Item to delete") @PathVariable(
          name = "order_item_id") Long orderItemId) {
    log.debug("REST request to delete the Item {} to the order {}", orderItemId, orderId);
    orderService.deleteOrderItem(orderId, orderItemId);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(ORDER_ITEM, orderItemId.toString())).build();
  }
}
