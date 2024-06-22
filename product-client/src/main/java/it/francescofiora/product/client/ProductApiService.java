package it.francescofiora.product.client;

import feign.Headers;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.service.dto.NewOrderDto;
import it.francescofiora.product.api.service.dto.NewOrderItemDto;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.OrderDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleOrderDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.client.config.FeignProductConfiguration;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Actuator Client Service.
 */
@FeignClient(name = "PRODUCT-API", configuration = FeignProductConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface ProductApiService {

  /**
   * Get the info.
   *
   * @return the response
   */
  @GetMapping("/actuator/info")
  ResponseEntity<String> getInfo();

  /**
   * Get the health.
   *
   * @return the response
   */
  @GetMapping("/actuator/health")
  ResponseEntity<String> getHealth();

  /**
   * Create a new category.
   *
   * @param categoryDto the entity to create
   * @return the result
   */
  @PostMapping("/api/v1/categories")
  ResponseEntity<Void> createCategory(NewCategoryDto categoryDto);

  /**
   * Update a category.
   *
   * @param categoryDto the entity to update
   * @param id the id of the entity
   * @return the result
   */
  @PutMapping("/api/v1/categories/{id}")
  ResponseEntity<Void> updateCategory(CategoryDto categoryDto, @PathVariable("id") Long id);

  /**
   * Find categories by name and description.
   *
   * @param name the name
   * @param description the description
   * @param pageable the pagination information
   * @return the list of categories
   */
  @GetMapping("/api/v1/categories")
  ResponseEntity<List<CategoryDto>> findCategories(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "description", required = false) String description,
      Pageable pageable);

  /**
   * Get the category by id.
   *
   * @param id the id of the entity
   * @return the category
   */
  @GetMapping("/api/v1/categories/{id}")
  ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Long id);

  /**
   * Delete a category by id.
   *
   * @param id the id of the entity
   * @return the result
   */
  @DeleteMapping("/api/v1/categories/{id}")
  ResponseEntity<Void> deleteCategoryById(@PathVariable("id") Long id);

  /**
   * Create a new product.
   *
   * @param productDto the Product to create
   * @return the result
   */
  @PostMapping("/api/v1/products")
  ResponseEntity<Void> createProduct(NewProductDto productDto);

  /**
   * Updates an existing product.
   *
   * @param productDto the product to update
   * @param id the id of the entity
   * @return the result
   */
  @PutMapping("/api/v1/products/{id}")
  ResponseEntity<Void> updateProduct(UpdatebleProductDto productDto, @PathVariable("id") Long id);

  /**
   * Find products by name, description and category id.
   *
   * @param name the name
   * @param description the description
   * @param categoryId the id of the category
   * @param pageable the pagination information
   * @return the list of products
   */
  @GetMapping("/api/v1/products")
  ResponseEntity<List<ProductDto>> findProducts(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "categoryId", required = false) Long categoryId,
      Pageable pageable);

  /**
   * Get the product by id.
   *
   * @param id the id of the product to retrieve
   * @return the product
   */
  @GetMapping("/api/v1/products/{id}")
  ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id);

  /**
   * Delete the product by id.
   *
   * @param id the id of the product to delete
   * @return the result
   */
  @DeleteMapping("/api/v1/products/{id}")
  ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id);

  /**
   * Create a new order.
   *
   * @param orderDto the order to create
   * @return the result
   */
  @PostMapping("/api/v1/orders")
  ResponseEntity<Void> createOrder(NewOrderDto orderDto);

  /**
   * Patches an existing order.
   *
   * @param orderDto the order to patch
   * @param id the id of the entity
   * @return the result
   */
  @PatchMapping("/api/v1/orders/{id}")
  ResponseEntity<Void> patchOrder(UpdatebleOrderDto orderDto, @PathVariable("id") Long id);

  /**
   * Find orders by code, customer and status.
   *
   * @param code the code
   * @param customer the customer
   * @param status the order status
   * @param pageable the pagination information
   * @return the list of orders
   */
  @GetMapping("/api/v1/orders")
  ResponseEntity<List<OrderDto>> findOrders(
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "customer", required = false) String customer,
      @RequestParam(value = "status", required = false) OrderStatus status,
      Pageable pageable);

  /**
   * Get the order by id.
   *
   * @param id the id of the order to retrieve
   * @return the order
   */
  @GetMapping("/api/v1/orders/{id}")
  public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id);

  /**
   * Delete the order by id.
   *
   * @param id the id of the order to delete
   * @return the result
   */
  @DeleteMapping("/api/v1/orders/{id}")
  ResponseEntity<Void> deleteOrderById(@PathVariable("id") Long id);

  /**
   * Add an Item to the Order.
   *
   * @param id Order id
   * @param orderItemDto the new item to add
   * @return the result
   */
  @PostMapping("/api/v1/orders/{id}/items")
  ResponseEntity<Void> addOrderItem(@PathVariable("id") Long id, NewOrderItemDto orderItemDto);

  /**
   * Delete the item by id.
   *
   * @param orderId the id of the order
   * @param orderItemId the id of the item to delete
   * @return the result
   */
  @DeleteMapping("/api/v1/orders/{order_id}/items/{order_item_id}")
  ResponseEntity<Void> deleteOrderItemById(
      @PathVariable(name = "order_id") Long orderId,
      @PathVariable(name = "order_item_id") Long orderItemId);
}
