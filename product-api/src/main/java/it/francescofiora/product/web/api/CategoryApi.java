package it.francescofiora.product.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.service.CategoryService;
import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link it.francescofiora.product.domain.Category}.
 */
@RestController
@RequestMapping("/api")
public class CategoryApi extends AbstractApi {

  private final Logger log = LoggerFactory.getLogger(CategoryApi.class);

  private static final String ENTITY_NAME = "CategoryDto";

  private final CategoryService categoryService;

  public CategoryApi(CategoryService categoryService) {
    super(ENTITY_NAME);
    this.categoryService = categoryService;
  }

  /**
   * {@code POST  /categories} : Create a new category.
   *
   * @param categoryDto the categoryDto to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *         categoryDto, or with status {@code 400 (Bad Request)} if the category has already an
   *         ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(summary = "Add new Category", description = "Add a new Category to the system",
      tags = {"category"})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Category created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "409", description = "An existing Category already exists")})
  @PostMapping("/categories")
  public ResponseEntity<Void> createCategory(
      @Parameter(description = "Add new Category") @Valid @RequestBody NewCategoryDto categoryDto)
      throws URISyntaxException {
    log.debug("REST request to create a new Category : {}", categoryDto);
    CategoryDto result = categoryService.create(categoryDto);
    return postResponse("/api/categories/" + result.getId(), result.getId());
  }

  /**
   * {@code PUT  /categories} : Updates an existing category.
   *
   * @param categoryDto the categoryDto to update.
   * @param id the id of the category to update
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}, or with status
   *         {@code 400 (Bad Request)} if the categoryDto is not valid, or with status
   *         {@code 500 (Internal Server Error)} if the categoryDto couldn't be updated.
   */
  @Operation(summary = "Update Category", description = "Update an Category to the system",
      tags = {"category"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Category updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/categories/{id}")
  public ResponseEntity<Void> updateCategory(
      @Parameter(description = "Category to update") @Valid @RequestBody CategoryDto categoryDto,
      @Parameter(description = "The id of the category to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    log.debug("REST request to update Category : {}", categoryDto);
    if (!id.equals(categoryDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(categoryDto.getId()),
          "Invalid id");
    }
    categoryService.update(categoryDto);
    return putResponse(id);
  }

  /**
   * {@code GET  /categories} : get all the productCategories.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *         productCategories in body.
   */
  @Operation(summary = "Searches Categories",
      description = "By passing in the appropriate options, "
          + "you can search for available categories in the system",
      tags = {"category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/categories")
  public ResponseEntity<List<CategoryDto>> getAllProductCategories(Pageable pageable) {
    log.debug("REST request to get all ProductCategories");
    return getResponse(categoryService.findAll(pageable));
  }

  /**
   * {@code GET  /categories/:id} : get the "id" category.
   *
   * @param id the id of the categoryDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryDto,
   *         or with status {@code 404 (Not Found)}.
   */
  @Operation(summary = "Searches Category by 'id'", description = "Searches Category by 'id'",
      tags = {"category"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = CategoryDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/categories/{id}")
  public ResponseEntity<CategoryDto> getCategory(
      @Parameter(description = "Id of the Category to get", required = true,
          example = "1") @PathVariable Long id) {
    log.debug("REST request to get Category : {}", id);
    return getResponse(categoryService.findOne(id), id);
  }

  /**
   * {@code DELETE  /categories/:id} : delete the "id" category.
   *
   * @param id the id of the categoryDto to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @Operation(summary = "Delete Category", description = "Delete an Category to the system",
      tags = {"category"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Category deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/categories/{id}")
  public ResponseEntity<Void> deleteCategory(
      @Parameter(description = "Id of the Category to delete", required = true,
          example = "1") @PathVariable Long id) {
    log.debug("REST request to delete Category : {}", id);
    categoryService.delete(id);
    return deleteResponse(id);
  }
}
