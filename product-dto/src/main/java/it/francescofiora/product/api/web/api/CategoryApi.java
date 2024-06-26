package it.francescofiora.product.api.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for managing Category.
 */
public interface CategoryApi {

  /**
   * TAG reference.
   */
  String TAG = "category";

  /**
   * Create a new category.
   *
   * @param categoryDto the category to create
   * @return the result
   */
  @Operation(summary = "Add new Category", description = "Add a new Category to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Category created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized")})
  @PostMapping("/api/v1/categories")
  ResponseEntity<Void> createCategory(
      @Parameter(description = "Add new Category") @Valid @RequestBody NewCategoryDto categoryDto);

  /**
   * Updates an existing category.
   *
   * @param categoryDto the category to update
   * @param id the id of the category to update
   * @return the result
   */
  @Operation(summary = "Update Category", description = "Update an Category to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Category updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/api/v1/categories/{id}")
  ResponseEntity<Void> updateCategory(
      @Parameter(description = "Category to update") @Valid @RequestBody CategoryDto categoryDto,
      @Parameter(description = "The id of the category to update", required = true,
          example = "1") @PathVariable("id") Long id);

  /**
   * Find categories by name and description.
   *
   * @param name the name
   * @param description the description
   * @param pageable the pagination information
   * @return the list of categories
   */
  @Operation(summary = "Searches Categories",
      description = "By passing in the appropriate options, "
          + "you can search for available categories in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/api/v1/categories")
  ResponseEntity<List<CategoryDto>> findCategories(
      @Parameter(description = "Name", example = "Shirt", in = ParameterIn.QUERY)
      @RequestParam(value = "name", required = false) String name,
      @Parameter(description = "Description of the category", example = "Shirt",
          in = ParameterIn.QUERY)
      @RequestParam(value = "description", required = false) String description,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable);

  /**
   * Get the category by id.
   *
   * @param id the id of the category to retrieve
   * @return the category
   */
  @Operation(summary = "Searches Category by 'id'", description = "Searches Category by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = CategoryDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/api/v1/categories/{id}")
  ResponseEntity<CategoryDto> getCategoryById(
      @Parameter(description = "Id of the Category to get", required = true,
          example = "1") @PathVariable("id") Long id);

  /**
   * Delete a category by id.
   *
   * @param id the id of the category to delete
   * @return the result
   */
  @Operation(summary = "Delete Category", description = "Delete an Category to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Category deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "403", description = "Forbidden, User not authorized"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/api/v1/categories/{id}")
  ResponseEntity<Void> deleteCategoryById(
      @Parameter(description = "Id of the Category to delete", required = true,
          example = "1") @PathVariable("id") Long id);
}
