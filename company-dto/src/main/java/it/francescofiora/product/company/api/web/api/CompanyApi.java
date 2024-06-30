package it.francescofiora.product.company.api.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
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
 * REST controller for managing Company.
 */
public interface CompanyApi {

  /**
   * TAG reference.
   */
  String TAG = "company";

  /**
   * Create a new company.
   *
   * @param companyDto the company to create
   * @return the result
   */
  @Operation(summary = "Add new Company", description = "Add a new Company to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Company created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/api/v1/companies")
  ResponseEntity<Void> createCompany(
      @Parameter(description = "Add new Company") @Valid @RequestBody NewCompanyDto companyDto);

  /**
   * Update an existing company.
   *
   * @param companyDto the company to update
   * @param id the id of the company
   * @return the result
   */
  @Operation(summary = "Update Company", description = "Update a Company to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Company updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/api/v1/companies/{id}")
  ResponseEntity<Void> updateCompany(
      @Parameter(description = "Company to update")
      @Valid @RequestBody UpdatebleCompanyDto companyDto,
      @Parameter(description = "The id of the company to update", required = true, example = "1")
      @PathVariable("id") Long id);

  /**
   * Find companies by name.
   *
   * @param name the name of the company
   * @param pageable the pagination information
   * @return the list of companies
   */
  @Operation(summary = "Searches Companies",
      description = "By passing in the appropriate options, "
          + "you can search for available Companies in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = CompanyDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/api/v1/companies")
  ResponseEntity<List<CompanyDto>> findCompanies(
      @Parameter(description = "Company name", example = "Groupon", in = ParameterIn.QUERY)
      @RequestParam(value = "name", required = false) String name,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}", in = ParameterIn.QUERY)
      Pageable pageable);

  /**
   * Get the company by id.
   *
   * @param id the id of the company to retrieve
   * @return the company
   */
  @Operation(summary = "Searches Company by 'id'", description = "Searches Company by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = CompanyDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/api/v1/companies/{id}")
  ResponseEntity<CompanyDto> getCompanyById(
      @Parameter(description = "Id of the Company to get", required = true, example = "1")
      @PathVariable("id") Long id);

  /**
   * Delete the company by id.
   *
   * @param id the id of the company to delete
   * @return the result
   */
  @Operation(summary = "Delete Company", description = "Delete an Company to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Company deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/api/v1/companies/{id}")
  ResponseEntity<Void> deleteCompanyById(
      @Parameter(description = "The id of the Company to delete", required = true, example = "1")
      @PathVariable("id") Long id);

  /**
   * Add un Address to the Company.
   *
   * @param id the id of the company
   * @param addressDto the new address to add
   * @return the result
   */
  @Operation(summary = "Add Address", description = "Add a new address to an Company", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Address added"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PostMapping("/api/v1/companies/{id}/addresses")
  ResponseEntity<Void> addAddress(
      @Parameter(description = "Company id", required = true, example = "1")
      @PathVariable("id") Long id,
      @Parameter(description = "Address to add") @Valid @RequestBody NewAddressDto addressDto);

  /**
   * Get the address by id.
   *
   * @param companyId the id of the company
   * @param addressId the id of the address
   * @return the address
   */
  @Operation(summary = "Searches Address by 'id'", description = "Searches Address by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = AddressDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/api/v1/companies/{company_id}/addresses/{address_id}")
  ResponseEntity<AddressDto> getAddressById(
      @Parameter(description = "The id of the Company", required = true, example = "1")
      @PathVariable("company_id") Long companyId,
      @Parameter(description = "Id of the Address to get", required = true, example = "1")
      @PathVariable("address_id") Long addressId);

  /**
   * Update an existing address.
   *
   * @param addressDto the address to update
   * @param companyId the id of the company
   * @param addressId the id of the address
   * @return the result
   */
  @Operation(summary = "Update Address", description = "Update an Address to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Address updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/api/v1/companies/{company_id}/addresses/{address_id}")
  ResponseEntity<Void> updateAddress(
      @Parameter(description = "Address to update")
      @Valid @RequestBody UpdatebleAddressDto addressDto,
      @Parameter(description = "The id of the Company", required = true, example = "1")
      @PathVariable("company_id") Long companyId,
      @Parameter(description = "The id of the Address to update", required = true, example = "1")
      @PathVariable("address_id") Long addressId);

  /**
   * Find the addresses of a company.
   *
   * @param id the id of the company
   * @param pageable the pagination information
   * @return the {@link ResponseEntity} with the list of companies
   */
  @Operation(summary = "Searches Companies",
      description = "By passing in the appropriate options, "
          + "you can search for available Companies in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = CompanyDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/api/v1/companies/{id}/addresses")
  ResponseEntity<List<AddressDto>> findAddresses(
      @Parameter(description = "Company id", required = true, example = "1")
      @PathVariable("id") Long id,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}", in = ParameterIn.QUERY)
      Pageable pageable);

  /**
   * Delete the address by id.
   *
   * @param companyId the id of the company
   * @param addressId the id of the address to delete
   * @return the result
   */
  @Operation(summary = "Delete address of a Company",
      description = "Delete an address of a Company", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Address deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/api/v1/companies/{company_id}/addresses/{address_id}")
  ResponseEntity<Void> deleteAddressById(
      @Parameter(description = "Id of the Company", required = true, example = "1")
      @PathVariable(name = "company_id") Long companyId,
      @Parameter(description = "Id of the Address to delete", required = true, example = "1")
      @PathVariable(name = "address_id") Long addressId);
}
