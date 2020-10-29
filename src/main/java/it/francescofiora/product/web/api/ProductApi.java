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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link it.francescofiora.product.domain.Product}.
 */
@RestController
@RequestMapping("/api")
public class ProductApi {

  private final Logger log = LoggerFactory.getLogger(ProductApi.class);

  private static final String ENTITY_NAME = "Product";

  private final ProductService productService;

  public ProductApi(ProductService productService) {
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
  @Operation(summary = "add new Product", description = "add a new Product to the system",
      tags = {"product"})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Product created"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "409", description = "an existing Product already exists")})
  @PostMapping("/products")
  public ResponseEntity<Void> createProduct(
      @Parameter(description = "add new Product") @Valid @RequestBody NewProductDto productDto)
      throws URISyntaxException {
    log.debug("REST request to create Product : {}", productDto);
    ProductDto result = productService.create(productDto);
    return ResponseEntity.created(new URI("/api/products/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .build();
  }

  /**
   * {@code PUT  /products} : Updates an existing product.
   *
   * @param productDto the product to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} or with status
   *         {@code 400 (Bad Request)} if the productDto is not valid, or with status
   *         {@code 500 (Internal Server Error)} if the productDto couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(summary = "update Product", description = "update an Product to the system",
      tags = {"product"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Product updated"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @PutMapping("/products")
  public ResponseEntity<Void> updateProduct(
      @Parameter(
          description = "Product to update") @Valid @RequestBody UpdatebleProductDto productDto)
      throws URISyntaxException {
    log.debug("REST request to update Product : {}", productDto);
    if (productDto.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    productService.update(productDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productDto.getId().toString()))
        .build();
  }

  /**
   * {@code GET  /products} : get all the products.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in
   *         body.
   */
  @Operation(summary = "searches Products", description = "By passing in the appropriate options, "
      + "you can search for available products in the system", tags = {"product"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
      @ApiResponse(responseCode = "400", description = "bad input parameter")})
  @GetMapping("/products")
  public ResponseEntity<List<ProductDto>> getAllProducts(Pageable pageable) {
    log.debug("REST request to get a page of Products");
    Page<ProductDto> page = productService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /products/:id} : get the "id" product.
   *
   * @param id the id of the productDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDto,
   *         or with status {@code 404 (Not Found)}.
   */
  @Operation(summary = "searches Product by 'id'", description = "searches Product by 'id'",
      tags = {"product"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "search results matching criteria",
          content = @Content(schema = @Schema(implementation = ProductDto.class))),
      @ApiResponse(responseCode = "400", description = "bad input parameter"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @GetMapping("/products/{id}")
  public ResponseEntity<ProductDto> getProduct(@Parameter(description = "id of the Product to get",
      required = true, example = "1") @PathVariable Long id) {
    log.debug("REST request to get Product : {}", id);
    Optional<ProductDto> productDto = productService.findOne(id);
    return ResponseUtil.wrapOrNotFound(ENTITY_NAME, productDto);
  }

  /**
   * {@code DELETE  /products/:id} : delete the "id" product.
   *
   * @param id the id of the productDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(summary = "delete Product", description = "delete an Product to the system",
      tags = {"product"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Product deleted"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @DeleteMapping("/products/{id}")
  public ResponseEntity<Void> deleteProduct(@Parameter(description = "id of the Product to delete",
      required = true, example = "1") @PathVariable Long id) {
    log.debug("REST request to delete Product : {}", id);
    productService.delete(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
