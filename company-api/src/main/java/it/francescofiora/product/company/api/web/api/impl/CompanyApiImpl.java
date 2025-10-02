package it.francescofiora.product.company.api.web.api.impl;

import it.francescofiora.product.company.api.service.CompanyService;
import it.francescofiora.product.company.api.web.api.CompanyApi;
import it.francescofiora.product.company.api.web.errors.BadRequestAlertException;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller implementation for managing Company.
 */
@RestController
public class CompanyApiImpl extends AbstractApi implements CompanyApi {

  private static final String ENTITY_NAME = "CompanyDto";
  private static final String ENTITY_COMPANY_ADDRESS = "AddressDto";

  private final CompanyService companyService;

  protected CompanyApiImpl(CompanyService companyService) {
    super(ENTITY_NAME);
    this.companyService = companyService;
  }

  @Override
  public ResponseEntity<Void> createCompany(NewCompanyDto companyDto) {
    var result = companyService.create(companyDto);
    return postResponse("/api/v1/companies/" + result.getId(), result.getId());
  }

  @Override
  public ResponseEntity<Void> updateCompany(Long id, UpdatebleCompanyDto companyDto) {
    if (!id.equals(companyDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(companyDto.getId()),
          "Invalid id");
    }
    companyService.update(companyDto);
    return putResponse(id);
  }

  @Override
  public ResponseEntity<List<CompanyDto>> findCompanies(String name, Pageable pageable) {
    return getResponse(companyService.findAll(name, pageable));
  }

  @Override
  public ResponseEntity<CompanyDto> getCompanyById(Long id) {
    return getResponse(companyService.findOne(id), id);
  }

  @Override
  public ResponseEntity<Void> deleteCompanyById(Long id) {
    companyService.delete(id);
    return deleteResponse(id);
  }

  @Override
  public ResponseEntity<Void> addAddress(Long id, NewAddressDto addressDto) {
    var result = companyService.addAddress(id, addressDto);
    return postResponse(ENTITY_COMPANY_ADDRESS,
        "/api/v1/companies/" + id + "/addresses/" + result.getId(), result.getId());
  }

  @Override
  public ResponseEntity<AddressDto> getAddressById(Long companyId, Long addressId) {
    return getResponse(ENTITY_COMPANY_ADDRESS, companyService.findOneAddress(companyId, addressId),
        addressId);
  }

  @Override
  public ResponseEntity<Void> updateAddress(
      Long companyId, Long addressId, UpdatebleAddressDto addressDto) {
    if (!addressId.equals(addressDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(addressDto.getId()),
          "Invalid id");
    }
    companyService.updateAddress(companyId, addressDto);
    return putResponse(addressId);
  }

  @Override
  public ResponseEntity<List<AddressDto>> findAddresses(Long id, Pageable pageable) {
    return getResponse(companyService.findAllAddress(id, pageable));
  }

  @Override
  public ResponseEntity<Void> deleteAddressById(Long companyId, Long addressId) {
    companyService.deleteAddress(companyId, addressId);
    return deleteResponse(ENTITY_COMPANY_ADDRESS, addressId);
  }
}
