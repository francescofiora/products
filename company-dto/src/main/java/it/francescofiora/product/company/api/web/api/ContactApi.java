package it.francescofiora.product.company.api.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.UpdatebleContactDto;
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
 * REST controller for managing Contact.
 */
public interface ContactApi {

  /**
   * TAG reference.
   */
  String TAG = "contact";

  /**
   * Create a new contact.
   *
   * @param contactDto the contact to create
   * @return the result
   */
  @Operation(summary = "Add new Contact", description = "Add a new Contact to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Contact created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/api/v1/contacts")
  ResponseEntity<Void> createContact(
      @Parameter(description = "Add new Contact") @Valid @RequestBody NewContactDto contactDto);

  /**
   * Update an existing contact.
   *
   * @param contactDto the contact to update
   * @param id the id of the entity
   * @return the result
   */
  @Operation(summary = "Update Contact", description = "Update a Contact to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Contact updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/api/v1/contacts/{id}")
  ResponseEntity<Void> updateContact(
      @Parameter(description = "Contact to update")
      @Valid @RequestBody UpdatebleContactDto contactDto,
      @Parameter(description = "The id of the contact to update", required = true, example = "1")
      @PathVariable("id") Long id);

  /**
   * Find contacts by name.
   *
   * @param name the name of the contact
   * @param pageable the pagination information
   * @return the list of contacts
   */
  @Operation(summary = "Searches Contacts",
      description = "By passing in the appropriate options, "
          + "you can search for available Contacts in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/api/v1/contacts")
  ResponseEntity<List<ContactDto>> findContacts(
      @Parameter(description = "Contact name", example = "Groupon", in = ParameterIn.QUERY)
      @RequestParam(value = "name", required = false) String name,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}", in = ParameterIn.QUERY)
      Pageable pageable);

  /**
   * Get the contact by id.
   *
   * @param id the id of the contact to retrieve
   * @return the contact
   */
  @Operation(summary = "Searches Contact by 'id'", description = "Searches Contact by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = ContactDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/api/v1/contacts/{id}")
  ResponseEntity<ContactDto> getContactById(
      @Parameter(description = "Id of the Contact to get", required = true, example = "1")
      @PathVariable("id") Long id);

  /**
   * Delete the contact by id.
   *
   * @param id the id of the contact to delete
   * @return the result
   */
  @Operation(summary = "Delete Contact", description = "Delete an Contact to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Contact deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/api/v1/contacts/{id}")
  ResponseEntity<Void> deleteContactById(
      @Parameter(description = "The id of the Contact to delete", required = true, example = "1")
      @PathVariable("id") Long id);
}
