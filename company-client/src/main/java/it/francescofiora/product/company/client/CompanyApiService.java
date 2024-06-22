package it.francescofiora.product.company.client;

import feign.Headers;
import it.francescofiora.product.company.client.config.FeignCompanyConfiguration;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleContactDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
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
 * Actuator Client Service.
 */
@FeignClient(name = "COMPANY-API", configuration = FeignCompanyConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface CompanyApiService {

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
   * Create a new contact.
   *
   * @param contactDto the contact to create
   * @return the result
   */
  @PostMapping("/api/v1/contacts")
  ResponseEntity<Void> createContact(@Valid @RequestBody NewContactDto contactDto);

  /**
   * Update an existing contact.
   *
   * @param contactDto the contact to update
   * @param id the id of the entity
   * @return the result
   */
  @PutMapping("/api/v1/contacts/{id}")
  ResponseEntity<Void> updateContact(
      @Valid @RequestBody UpdatebleContactDto contactDto,
      @PathVariable("id") Long id);

  /**
   * Find contacts by name.
   *
   * @param name the name of the contact
   * @param pageable the pagination information
   * @return the list of contacts
   */
  @GetMapping("/api/v1/contacts")
  ResponseEntity<List<ContactDto>> findContacts(
      @RequestParam(value = "name", required = false) String name, Pageable pageable);

  /**
   * Get the contact by id.
   *
   * @param id the id of the contact to retrieve
   * @return the contact
   */
  @GetMapping("/api/v1/contacts/{id}")
  ResponseEntity<ContactDto> getContactById(@PathVariable("id") Long id);

  /**
   * Delete the contact by id.
   *
   * @param id the id of the contact to delete
   * @return the result
   */
  @DeleteMapping("/api/v1/contacts/{id}")
  ResponseEntity<Void> deleteContactById(@PathVariable("id") Long id);

  /**
   * Create a new company.
   *
   * @param companyDto the company to create
   * @return the result
   */
  @PostMapping("/api/v1/companies")
  ResponseEntity<Void> createCompany(@Valid @RequestBody NewCompanyDto companyDto);

  /**
   * Update an existing company.
   *
   * @param companyDto the company to update
   * @param id the id of the company
   * @return the result
   */
  @PutMapping("/api/v1/companies/{id}")
  ResponseEntity<Void> updateCompany(
      @Valid @RequestBody UpdatebleCompanyDto companyDto,
      @PathVariable("id") Long id);

  /**
   * Find companies by name.
   *
   * @param name the name of the company
   * @param pageable the pagination information
   * @return the list of companies
   */
  @GetMapping("/api/v1/companies")
  ResponseEntity<List<CompanyDto>> findCompanies(
      @RequestParam(value = "name", required = false) String name, Pageable pageable);

  /**
   * Get the company by id.
   *
   * @param id the id of the company to retrieve
   * @return the company
   */
  @GetMapping("/api/v1/companies/{id}")
  ResponseEntity<CompanyDto> getCompanyById(@PathVariable("id") Long id);

  /**
   * Delete the company by id.
   *
   * @param id the id of the company to delete
   * @return the result
   */
  @DeleteMapping("/api/v1/companies/{id}")
  ResponseEntity<Void> deleteCompanyById(@PathVariable("id") Long id);

  /**
   * Add un Address to the Company.
   *
   * @param id the id of the company
   * @param addressDto the new address to add
   * @return the result
   */
  @PostMapping("/api/v1/companies/{id}/addresses")
  ResponseEntity<Void> addAddress(@PathVariable("id") Long id, NewAddressDto addressDto);


  /**
   * Get the address by id.
   *
   * @param companyId the id of the company
   * @param addressId the id of the address
   * @return the address
   */
  @GetMapping("/api/v1/companies/{company_id}/addresses/{address_id}")
  public ResponseEntity<AddressDto> getAddressById(
      @PathVariable("company_id") Long companyId,
      @PathVariable("address_id") Long addressId);

  /**
   * Update an existing address.
   *
   * @param addressDto the address to update
   * @param companyId the id of the company
   * @param addressId the id of the address
   * @return the result
   */
  @PutMapping("/api/v1/companies/{company_id}/addresses/{address_id}")
  public ResponseEntity<Void> updateAddress(
      @Valid @RequestBody UpdatebleAddressDto addressDto,
      @PathVariable("company_id") Long companyId,
      @PathVariable("address_id") Long addressId);

  /**
   * Find the addresses of a company.
   *
   * @param id the id of the company
   * @param pageable the pagination information
   * @return the list of addresses
   */
  @GetMapping("/api/v1/companies/{id}/addresses")
  public ResponseEntity<List<AddressDto>> findAddresses(
      @PathVariable("id") Long id, Pageable pageable);

  /**
   * Delete the address by id.
   *
   * @param companyId the id of the company
   * @param addressId the id of the address to delete
   * @return the result
   */
  @DeleteMapping("/api/v1/companies/{company_id}/addresses/{address_id}")
  ResponseEntity<Void> deleteAddressById(
      @PathVariable(name = "company_id") Long companyId,
      @PathVariable(name = "address_id") Long addressId);
}
