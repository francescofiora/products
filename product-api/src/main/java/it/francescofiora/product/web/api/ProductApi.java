package it.francescofiora.product.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.service.ProductService;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.ProductDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import it.francescofiora.product.web.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link it.francescofiora.product.domain.Product}.
 */
@RestController
@RequestMapping("/api")
public class ProductApi extends AbstractApi {

  private final Logger log = LoggerFactory.getLogger(ProductApi.class);

  private static final String ENTITY_NAME = "ProductDto";

  private final ProductService productService;

  public ProductApi(ProductService productService) {
    super(ENTITY_NAME);
    this.productService = productService;
  }

  /**
   * {@code POST  /products} : Create a new product.
   *
   * @param productDto the Product to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}, or with status
   *         {@code 400 (Bad Request)} if the product has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(summary = "Add new Product", description = "Add a new Product to the system",
      tags = {"product"})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Product created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized"),
      @ApiResponse(responseCode = "409", description = "An existing Product already exists")})
  @PostMapping("/products")
  @PreAuthorize(AUTHORIZE_ADMIN)
  public ResponseEntity<Void> createProduct(
      @Parameter(description = "Add new Product") @Valid @RequestBody NewProductDto productDto)
      throws URISyntaxException {
    log.debug("REST request to create Product : {}", productDto);
    var result = productService.create(productDto);
    return postResponse("/api/products/" + result.getId(), result.getId());
  }

  /**
   * {@code PUT  /products} : Updates an existing product.
   *
   * @param productDto the product to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} or with status
   *         {@code 400 (Bad Request)} if the productDto is not valid, or with status
   *         {@code 500 (Internal Server Error)} if the productDto couldn't be updated.
   */
  @Operation(summary = "Update Product", description = "Update an Product to the system",
      tags = {"product"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Product updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/products/{id}")
  @PreAuthorize(AUTHORIZE_ADMIN)
  public ResponseEntity<Void> updateProduct(
      @Parameter(
          description = "Product to update") @Valid @RequestBody UpdatebleProductDto productDto,
      @Parameter(description = "The id of the category to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    log.debug("REST request to update Product : {}", productDto);
    if (!id.equals(productDto.getId())) {
      throw new BadRequestAlertException("UpdatebleProductDto", String.valueOf(productDto.getId()),
          "Invalid id");
    }
    productService.update(productDto);
    return putResponse(id);
  }

  /**
   * {@code GET  /products} : get all the products.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in
   *         body.
   */
  @Operation(summary = "Searches Products",
      description = "By passing in the appropriate options, "
          + "you can search for available products in the system",
      tags = {"product"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/products")
  @PreAuthorize(AUTHORIZE_ALL)
  public ResponseEntity<List<ProductDto>> getAllProducts(Pageable pageable) {
    log.debug("REST request to get a page of Products");
    return getResponse(productService.findAll(pageable));
  }

  /**
   * {@code GET  /products/:id} : get the "id" product.
   *
   * @param id the id of the productDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDto,
   *         or with status {@code 404 (Not Found)}.
   */
  @Operation(summary = "Searches Product by 'id'", description = "Searches Product by 'id'",
      tags = {"product"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = ProductDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/products/{id}")
  @PreAuthorize(AUTHORIZE_ALL)
  public ResponseEntity<ProductDto> getProduct(@Parameter(description = "Id of the Product to get",
      required = true, example = "1") @PathVariable Long id) {
    log.debug("REST request to get Product : {}", id);
    return getResponse(productService.findOne(id), id);
  }

  /**
   * {@code DELETE  /products/:id} : delete the "id" product.
   *
   * @param id the id of the productDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(summary = "Delete Product", description = "Delete an Product to the system",
      tags = {"product"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Product deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/products/{id}")
  @PreAuthorize(AUTHORIZE_ADMIN)
  public ResponseEntity<Void> deleteProduct(@Parameter(description = "Id of the Product to delete",
      required = true, example = "1") @PathVariable Long id) {
    log.debug("REST request to delete Product : {}", id);
    productService.delete(id);
    return deleteResponse(id);
  }
}
