package it.francescofiora.product.company.api.service;

import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Company Service.
 */
public interface CompanyService {

  /**
   * Create a new company.
   *
   * @param companyDto the entity to create
   * @return the persisted entity
   */
  CompanyDto create(NewCompanyDto companyDto);

  /**
   * Create a new Address.
   *
   * @param companyId the id of the company
   * @param addressDto the NewAddressDto
   * @return the AddressDto
   */
  AddressDto addAddress(Long companyId, NewAddressDto addressDto);

  /**
   * Update a company.
   *
   * @param companyDto the entity to patch
   */
  void update(UpdatebleCompanyDto companyDto);

  /**
   * Update an address.
   *
   * @param companyId the id of the company
   * @param addressDto the UpdatebleAddressDto
   */
  void updateAddress(Long companyId, UpdatebleAddressDto addressDto);

  /**
   * Get all the companies.
   *
   * @param name the name
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<CompanyDto> findAll(String name, Pageable pageable);

  /**
   * Get all the addresses of a company.
   *
   * @param companyId the id of the company
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<AddressDto> findAllAddress(Long companyId, Pageable pageable);

  /**
   * Get the Company by "id" .
   *
   * @param id the id of the entity
   * @return the entity
   */
  Optional<CompanyDto> findOne(Long id);

  /**
   * Delete the Company by "id" .
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * Delete the Address from the company.
   *
   * @param companyId the id of the Company
   * @param addressId the id of the Address
   */
  void deleteAddress(Long companyId, Long addressId);

  /**
   * Get the Address by "addressId" of the Company "companyId".
   *
   * @param companyId the id of the Company
   * @param addressId the id of the Address
   */
  Optional<AddressDto> findOneAddress(Long companyId, Long addressId);
}
