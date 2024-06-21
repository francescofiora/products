package it.francescofiora.product.company.api.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.francescofiora.product.company.api.service.CompanyService;
import it.francescofiora.product.company.api.web.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link CompanyDto}.
 */
@RestController
@RequestMapping("/api/v1")
public class CompanyApi extends AbstractApi {

  private static final String ENTITY_NAME = "CompanyDto";
  private static final String ENTITY_COMPANY_ADDRESS = "AddressDto";
  private static final String TAG = "company";

  private final CompanyService companyService;

  protected CompanyApi(CompanyService companyService) {
    super(ENTITY_NAME);
    this.companyService = companyService;
  }

  /**
   * {@code POST  /companies} : Create a new company.
   *
   * @param companyDto the company to create
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Add new Company", description = "Add a new Company to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Company created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/companies")
  public ResponseEntity<Void> createCompany(
      @Parameter(description = "Add new Company") @Valid @RequestBody NewCompanyDto companyDto) {
    var result = companyService.create(companyDto);
    return postResponse("/api/v1/companies/" + result.getId(), result.getId());
  }

  /**
   * {@code PUT  /companies} : Update an existing company.
   *
   * @param companyDto the company to update
   * @param id the id of the company
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Update Company", description = "Update a Company to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Company updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/companies/{id}")
  public ResponseEntity<Void> updateCompany(
      @Parameter(
          description = "Company to update") @Valid @RequestBody UpdatebleCompanyDto companyDto,
      @Parameter(description = "The id of the company to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    if (!id.equals(companyDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(companyDto.getId()),
          "Invalid id");
    }
    companyService.update(companyDto);
    return putResponse(id);
  }

  /**
   * {@code GET  /companies} : get all the companies.
   *
   * @param name the name of the company
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
  @GetMapping("/companies")
  public ResponseEntity<List<CompanyDto>> getAllCompanies(
      @Parameter(description = "Company name", example = "Groupon",
          in = ParameterIn.QUERY) @RequestParam(required = false) String name,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable) {
    return getResponse(companyService.findAll(name, pageable));
  }

  /**
   * {@code GET  /companies/:id} : get the "id" company.
   *
   * @param id the id of the company to retrieve
   * @return the {@link ResponseEntity} with the company
   */
  @Operation(summary = "Searches Company by 'id'", description = "Searches Company by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = CompanyDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/companies/{id}")
  public ResponseEntity<CompanyDto> getCompany(@Parameter(description = "Id of the Company to get",
      required = true, example = "1") @PathVariable Long id) {
    return getResponse(companyService.findOne(id), id);
  }

  /**
   * {@code DELETE  /companies/:id} : delete the company by id.
   *
   * @param id the id of the company to delete
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Delete Company", description = "Delete an Company to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Company deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/companies/{id}")
  public ResponseEntity<Void> deleteCompany(
      @Parameter(description = "The id of the Company to delete", required = true,
          example = "1") @PathVariable Long id) {
    companyService.delete(id);
    return deleteResponse(id);
  }

  /**
   * Add Address to the Company.
   *
   * @param id the id of the company
   * @param addressDto the new address to add
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Add Address", description = "Add a new address to an Company", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Address added"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PostMapping("/companies/{id}/addresses")
  public ResponseEntity<Void> addAddress(
      @Parameter(description = "Company id", required = true, example = "1") @PathVariable Long id,
      @Parameter(description = "Address to add") @Valid @RequestBody NewAddressDto addressDto) {
    var result = companyService.addAddress(id, addressDto);
    return postResponse(ENTITY_COMPANY_ADDRESS,
        "/api/v1/companies/" + id + "/addresses/" + result.getId(), result.getId());
  }


  /**
   * {@code GET /companies/:id/addresses/:id} : get the address by id.
   *
   * @param companyId the id of the company
   * @param addressId the id of the address
   * @return the {@link ResponseEntity} with the address
   */
  @Operation(summary = "Searches Address by 'id'", description = "Searches Address by 'id'",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = AddressDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/companies/{company_id}/addresses/{address_id}")
  public ResponseEntity<AddressDto> getAddress(
      @Parameter(description = "The id of the Company", required = true,
          example = "1") @PathVariable("company_id") Long companyId,
      @Parameter(description = "Id of the Address to get", required = true,
          example = "1") @PathVariable("address_id") Long addressId) {
    return getResponse(ENTITY_COMPANY_ADDRESS, companyService.findOneAddress(companyId, addressId),
        addressId);
  }

  /**
   * {@code PUT /companies/:id/addresses/:id} : Update an existing address.
   *
   * @param addressDto the address to update
   * @param companyId the id of the company
   * @param addressId the id of the address
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Update Address", description = "Update an Address to the system",
      tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Address updated"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PutMapping("/companies/{company_id}/addresses/{address_id}")
  public ResponseEntity<Void> updateAddress(
      @Parameter(
          description = "Address to update") @Valid @RequestBody UpdatebleAddressDto addressDto,
      @Parameter(description = "The id of the Company", required = true,
          example = "1") @PathVariable("company_id") Long companyId,
      @Parameter(description = "The id of the Address to update", required = true,
          example = "1") @PathVariable("address_id") Long addressId) {
    if (!addressId.equals(addressDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(addressDto.getId()),
          "Invalid id");
    }
    companyService.updateAddress(companyId, addressDto);
    return putResponse(addressId);
  }

  /**
   * {@code GET /companies/:id/addresses} : get all addresses of a company.
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
  @GetMapping("/companies/{id}/addresses")
  public ResponseEntity<List<AddressDto>> getAllAddresses(
      @Parameter(description = "Company id", required = true, example = "1") @PathVariable Long id,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable) {
    return getResponse(companyService.findAllAddress(id, pageable));
  }

  /**
   * {@code DELETE  /companies/:id/addresses/:id} : delete the "id" item.
   *
   * @param companyId the id of the company
   * @param addressId the id of the address to delete
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Delete address of a Company",
      description = "Delete an address of a Company", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Address deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/companies/{company_id}/addresses/{address_id}")
  public ResponseEntity<Void> deleteAddress(
      @Parameter(description = "Id of the Company", required = true,
          example = "1") @PathVariable(name = "company_id") Long companyId,
      @Parameter(description = "Id of the Address to delete", required = true,
          example = "1") @PathVariable(name = "address_id") Long addressId) {
    companyService.deleteAddress(companyId, addressId);
    return deleteResponse(ENTITY_COMPANY_ADDRESS, addressId);
  }
}
