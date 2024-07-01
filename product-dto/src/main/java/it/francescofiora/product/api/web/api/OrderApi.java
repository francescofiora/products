package it.francescofiora.product.api.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for managing Order.
 */
public interface OrderApi {

  /**
   * TAG reference.
   */
  String TAG = "order";

  /**
   * Create a new order.
   *
   * @param orderDto the order to create
   * @return the result
   */
  @Operation(summary = "Add new Order", description = "Add a new Order to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Order created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/api/v1/orders")
  ResponseEntity<Void> createOrder(
      @Parameter(description = "Add new Order") @Valid @RequestBody NewOrderDto orderDto);

  /**
   * Patches an existing order.
   *
   * @param orderDto the order to patch
   * @param id the id of the order to update
   * @return the result
   */
  @Operation(summary = "Patch Order", description = "Patch an Order to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Order patched"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PatchMapping("/api/v1/orders/{id}")
  ResponseEntity<Void> patchOrder(
      @Parameter(description = "Order to update") @Valid @RequestBody UpdatebleOrderDto orderDto,
      @Parameter(description = "The id of the order to patch", required = true,
          example = "1") @PathVariable("id") Long id);

  /**
   * Find orders by code, customer and status.
   *
   * @param code the code
   * @param customerId the id of customer
   * @param status the order status
   * @param pageable the pagination information
   * @return the list of orders
   */
  @Operation(summary = "Searches Orders",
      description = "By passing in the appropriate options, "
          + "you can search for available categories in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/api/v1/orders")
  ResponseEntity<List<OrderDto>> findOrders(
      @Parameter(description = "Order code", example = "ORD_1", in = ParameterIn.QUERY)
      @RequestParam(value = "code", required = false) String code,
      @Parameter(description = "Customer Id", example = "1", in = ParameterIn.QUERY)
      @RequestParam(value = "customerId", required = false) Long customerId,
      @Parameter(description = "Status", example = "PENDING", in = ParameterIn.QUERY)
      @RequestParam(value = "status", required = false) OrderStatus status,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable);

  /**
   * Get the order by id.
   *
   * @param id the id of the order to retrieve
   * @return the order
   */
  @Operation(summary = "Searches Order by 'id'", description = "Searches Order by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = OrderDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/api/v1/orders/{id}")
  ResponseEntity<OrderDto> getOrderById(@Parameter(description = "Id of the Order to get",
      required = true, example = "1") @PathVariable("id") Long id);

  /**
   * Delete the order by id.
   *
   * @param id the id of the order to delete
   * @return the result
   */
  @Operation(summary = "Delete Order", description = "Delete an Order to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Order deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/api/v1/orders/{id}")
  ResponseEntity<Void> deleteOrderById(
      @Parameter(description = "The id of the Order to delete", required = true, example = "1")
      @PathVariable("id") Long id);

  /**
   * Add an Item to the Order.
   *
   * @param id Order id
   * @param orderItemDto the new item to add
   * @return the result
   */
  @Operation(summary = "Add OrderItem", description = "Add a new item to an Order", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OrderItem added"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PostMapping("/api/v1/orders/{id}/items")
  ResponseEntity<Void> addOrderItem(
      @Parameter(description = "Order id", required = true, example = "1")
      @PathVariable("id") Long id,
      @Parameter(description = "Item to add") @Valid @RequestBody NewOrderItemDto orderItemDto);

  /**
   * Delete the item by id.
   *
   * @param orderId the id of the order
   * @param orderItemId the id of the item to delete
   * @return the result
   */
  @Operation(summary = "Delete item of a Order", description = "Delete an item of a Order",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "OrderItem deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/api/v1/orders/{order_id}/items/{order_item_id}")
  ResponseEntity<Void> deleteOrderItemById(
      @Parameter(description = "Id of the Order", required = true,
          example = "1") @PathVariable(name = "order_id") Long orderId,
      @Parameter(description = "Id of the Item to delete", required = true,
          example = "1") @PathVariable(name = "order_item_id") Long orderItemId);
}
