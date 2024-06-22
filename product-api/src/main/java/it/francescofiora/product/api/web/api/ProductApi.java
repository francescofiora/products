package it.francescofiora.product.api.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.api.service.ProductService;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.service.dto.UpdatebleProductDto;
import it.francescofiora.product.api.web.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api/v1")
public class ProductApi extends AbstractApi {

  private static final String ENTITY_NAME = "ProductDto";
  private static final String TAG = "product";

  private final ProductService productService;

  public ProductApi(ProductService productService) {
    super(ENTITY_NAME);
    this.productService = productService;
  }

  /**
   * Create a new product.
   *
   * @param productDto the Product to create
   * @return the result
   */
  @Operation(summary = "Add new Product", description = "Add a new Product to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Product created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized")})
  @PostMapping("/products")
  public ResponseEntity<Void> createProduct(
      @Parameter(description = "Add new Product") @Valid @RequestBody NewProductDto productDto) {
    var result = productService.create(productDto);
    return postResponse("/api/v1/products/" + result.getId(), result.getId());
  }

  /**
   * Updates an existing product.
   *
   * @param productDto the product to update
   * @return the result
   */
  @Operation(summary = "Update Product", description = "Update an Product to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Product updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/products/{id}")
  public ResponseEntity<Void> updateProduct(
      @Parameter(
          description = "Product to update") @Valid @RequestBody UpdatebleProductDto productDto,
      @Parameter(description = "The id of the category to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    if (!id.equals(productDto.getId())) {
      throw new BadRequestAlertException("UpdatebleProductDto", String.valueOf(productDto.getId()),
          "Invalid id");
    }
    productService.update(productDto);
    return putResponse(id);
  }

  /**
   * Find products by name, description and category id.
   *
   * @param name the name
   * @param description the description
   * @param categoryId the id of the category
   * @param pageable the pagination information
   * @return the list of products
   */
  @Operation(summary = "Searches Products",
      description = "By passing in the appropriate options, "
          + "you can search for available products in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/products")
  public ResponseEntity<List<ProductDto>> findProducts(
      @Parameter(description = "Name", example = "SHIRTM01",
          in = ParameterIn.QUERY) @RequestParam(required = false) String name,
      @Parameter(description = "Description of the product", example = "Shirt for Men",
          in = ParameterIn.QUERY) @RequestParam(required = false) String description,
      @Parameter(description = "ID of the Category", example = "1",
          in = ParameterIn.QUERY) @RequestParam(required = false) Long categoryId,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable) {
    return getResponse(productService.findAll(name, description, categoryId, pageable));
  }

  /**
   * Get the product by id.
   *
   * @param id the id of the product to retrieve
   * @return the product
   */
  @Operation(summary = "Searches Product by 'id'", description = "Searches Product by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = ProductDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/products/{id}")
  public ResponseEntity<ProductDto> getProductById(
      @Parameter(description = "Id of the Product to get", required = true, example = "1")
      @PathVariable("id") Long id) {
    return getResponse(productService.findOne(id), id);
  }

  /**
   * Delete the product by id.
   *
   * @param id the id of the product to delete
   * @return the result
   */
  @Operation(summary = "Delete Product", description = "Delete an Product to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Product deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/products/{id}")
  public ResponseEntity<Void> deleteProductById(@Parameter(description = "Id of the Product to delete",
      required = true, example = "1") @PathVariable("id") Long id) {
    productService.delete(id);
    return deleteResponse(id);
  }
}
