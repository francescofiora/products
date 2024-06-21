package it.francescofiora.product.company.api.service.impl;

import it.francescofiora.product.company.api.domain.Address;
import it.francescofiora.product.company.api.domain.Company;
import it.francescofiora.product.company.api.repository.AddressRepository;
import it.francescofiora.product.company.api.repository.CompanyRepository;
import it.francescofiora.product.company.api.repository.ContactRepository;
import it.francescofiora.product.company.api.service.CompanyService;
import it.francescofiora.product.company.api.service.mapper.AddressMapper;
import it.francescofiora.product.company.api.service.mapper.CompanyMapper;
import it.francescofiora.product.company.api.web.errors.BadRequestAlertException;
import it.francescofiora.product.company.api.web.errors.NotFoundAlertException;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Company}.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();

  private static final String ADDRESS_ENTITY_NAME = "AddressDto";
  private static final String ENTITY_NAME = "CompanyDto";
  private static final String COMPANY_NOT_FOUND = "Company not found";
  private static final String ADDRESS_NOT_FOUND = "Address not found";
  private static final String DO_NOT_MATCH = "Company does not match with address";

  private final CompanyRepository companyRepository;
  private final AddressRepository addressRepository;
  private final ContactRepository contactRepository;
  private final CompanyMapper companyMapper;
  private final AddressMapper addressMapper;

  @Override
  public CompanyDto create(NewCompanyDto companyDto) {
    log.debug("Request to create a new Company : {}", companyDto);
    var company = companyMapper.toEntity(companyDto);
    company = companyRepository.save(company);
    for (var address : company.getAddresses()) {
      address.setCompany(company);
      addressRepository.save(address);
    }
    return companyMapper.toDto(company);
  }

  @Override
  public AddressDto addAddress(Long companyId, NewAddressDto addressDto) {
    log.debug("Request to create a new address {} of the Company {}", addressDto, companyId);
    var company = findCompanyById(companyId);
    var address = addressMapper.toEntity(addressDto);
    address.setCompany(company);
    address = addressRepository.save(address);
    return addressMapper.toDto(address);
  }

  @Override
  public void update(UpdatebleCompanyDto companyDto) {
    log.debug("Request to update Company : {}", companyDto);
    var company = findCompanyById(companyDto.getId());
    companyMapper.updateEntityFromDto(companyDto, company);
    companyRepository.save(company);
  }

  @Override
  public void updateAddress(Long companyId, UpdatebleAddressDto addressDto) {
    log.debug("Request to update the address {} of the Company {}", addressDto, companyId);
    var optAddress = addressRepository.findById(addressDto.getId());
    if (optAddress.isEmpty()) {
      throw new NotFoundAlertException(ADDRESS_ENTITY_NAME, String.valueOf(addressDto.getId()),
          ADDRESS_NOT_FOUND);
    }
    var address = optAddress.get();
    if (!companyId.equals(address.getCompany().getId())) {
      throw new BadRequestAlertException(ADDRESS_ENTITY_NAME, String.valueOf(companyId),
          "Invalid Company id");
    }
    addressMapper.updateEntityFromDto(addressDto, address);
    addressRepository.save(address);
  }

  private Company findCompanyById(Long id) {
    var optCompany = companyRepository.findById(id);
    if (optCompany.isEmpty()) {
      throw new NotFoundAlertException(ENTITY_NAME, String.valueOf(id), COMPANY_NOT_FOUND);
    }
    return optCompany.get();
  }

  @Override
  public Page<CompanyDto> findAll(String name, Pageable pageable) {
    log.debug("Request to get all Companies");
    var company = new Company();
    company.setName(name);
    var exampleMatcher = ExampleMatcher.matchingAll().withMatcher("name", PROPERTY_MATCHER_DEFAULT);
    var example = Example.of(company, exampleMatcher);
    return companyRepository.findAll(example, pageable).map(companyMapper::toDto);
  }

  @Override
  public Page<AddressDto> findAllAddress(Long companyId, Pageable pageable) {
    log.debug("Request to get all Addresses of the Company {}", companyId);
    var address = new Address();
    address.setCompany(new Company());
    address.getCompany().setId(companyId);
    var example = Example.of(address);
    return addressRepository.findAll(example, pageable).map(addressMapper::toDto);
  }

  @Override
  public Optional<CompanyDto> findOne(Long id) {
    log.debug("Request to get Company : {}", id);
    var company = companyRepository.findById(id);
    var result = company.map(companyMapper::toDto);
    if (result.isPresent()) {
      result.get()
          .setAddresses(company.get().getAddresses().stream().map(addressMapper::toDto).toList());
    }
    return result;
  }

  @Override
  public Optional<AddressDto> findOneAddress(Long companyId, Long addressId) {
    log.debug("Request to get the address {} of the Company {}", companyId, companyId);
    var optAddress = addressRepository.findById(addressId);
    if (optAddress.isPresent()) {
      var address = optAddress.get();
      if (!companyId.equals(address.getCompany().getId())) {
        throw new BadRequestAlertException(ADDRESS_ENTITY_NAME, String.valueOf(companyId),
            "Invalid Company id");
      }
    }
    return optAddress.map(addressMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Company : {}", id);
    var optCompany = companyRepository.findById(id);
    if (optCompany.isEmpty()) {
      return;
    }
    var company = optCompany.get();
    for (var address : company.getAddresses()) {
      address.getContacts().forEach(contactRepository::delete);
      addressRepository.delete(address);
    }
    companyRepository.deleteById(id);
  }

  @Override
  public void deleteAddress(Long companyId, Long addressId) {
    log.debug("Request to delete the Address {} of the Company {}", addressId, companyId);
    var optAddress = addressRepository.findById(addressId);
    if (optAddress.isEmpty()) {
      return;
    }
    var address = optAddress.get();
    if (!address.getCompany().getId().equals(companyId)) {
      throw new NotFoundAlertException(ADDRESS_ENTITY_NAME, String.valueOf(addressId),
          DO_NOT_MATCH);
    }
    address.getContacts().forEach(contactRepository::delete);
    addressRepository.delete(address);
  }
}
