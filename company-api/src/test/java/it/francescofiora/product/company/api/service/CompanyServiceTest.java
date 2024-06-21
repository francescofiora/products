package it.francescofiora.product.company.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.product.company.api.domain.Address;
import it.francescofiora.product.company.api.domain.Company;
import it.francescofiora.product.company.api.repository.AddressRepository;
import it.francescofiora.product.company.api.repository.CompanyRepository;
import it.francescofiora.product.company.api.repository.ContactRepository;
import it.francescofiora.product.company.api.service.impl.CompanyServiceImpl;
import it.francescofiora.product.company.api.service.mapper.AddressMapper;
import it.francescofiora.product.company.api.service.mapper.CompanyMapper;
import it.francescofiora.product.company.api.util.TestUtils;
import it.francescofiora.product.company.api.web.errors.NotFoundAlertException;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class CompanyServiceTest {

  private static final Long ID = 1L;
  private static final Long ADDRESS_ID = 2L;
  private static final Long CONTACT_ID = 3L;

  @Test
  void testCreate() {
    var company = TestUtils.createCompany(ID);
    company.getAddresses().add(TestUtils.createAddress(ADDRESS_ID));
    var companyMapper = mock(CompanyMapper.class);
    when(companyMapper.toEntity(any(NewCompanyDto.class))).thenReturn(company);

    var companyRepository = mock(CompanyRepository.class);
    when(companyRepository.save(any(Company.class))).thenReturn(company);

    var expected = TestUtils.createCompanyDto(ID);
    when(companyMapper.toDto(any(Company.class))).thenReturn(expected);

    var addressRepository = mock(AddressRepository.class);
    var companyDto = TestUtils.createNewCompanyDto();
    companyDto.getAddresses().add(TestUtils.createNewAddressDto());
    var companyService = new CompanyServiceImpl(companyRepository, addressRepository,
        mock(ContactRepository.class), companyMapper, mock(AddressMapper.class));
    var actual = companyService.create(companyDto);

    verify(companyRepository).save(any(Company.class));
    verify(addressRepository).save(any(Address.class));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testUpdateNotFound() {
    var companyDto = TestUtils.createUpdatebleCompanyDto(ID);
    var companyService =
        new CompanyServiceImpl(mock(CompanyRepository.class), mock(AddressRepository.class),
            mock(ContactRepository.class), mock(CompanyMapper.class), mock(AddressMapper.class));
    assertThrows(NotFoundAlertException.class, () -> companyService.update(companyDto));

    var addressDto = TestUtils.createUpdatebleAddressDto(ADDRESS_ID);
    var addressService =
        new CompanyServiceImpl(mock(CompanyRepository.class), mock(AddressRepository.class),
            mock(ContactRepository.class), mock(CompanyMapper.class), mock(AddressMapper.class));
    assertThrows(NotFoundAlertException.class, () -> addressService.updateAddress(ID, addressDto));
  }

  @Test
  void testUpdate() {
    var company = TestUtils.createCompany(ID);
    var companyRepository = mock(CompanyRepository.class);
    when(companyRepository.findById(ID)).thenReturn(Optional.of(company));

    var companyDto = TestUtils.createUpdatebleCompanyDto(ID);
    companyDto.setId(ID);
    var companyMapper = mock(CompanyMapper.class);
    var companyService = new CompanyServiceImpl(companyRepository, mock(AddressRepository.class),
        mock(ContactRepository.class), companyMapper, mock(AddressMapper.class));
    companyService.update(companyDto);
    verify(companyMapper).updateEntityFromDto(companyDto, company);
    verify(companyRepository).save(company);
  }

  @Test
  void testUpdateAddress() {
    var address = TestUtils.createAddress(ADDRESS_ID);
    address.setCompany(TestUtils.createCompany(ID));
    var addressRepository = mock(AddressRepository.class);
    when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(address));

    var addressMapper = mock(AddressMapper.class);
    var addressService = new CompanyServiceImpl(mock(CompanyRepository.class), addressRepository,
        mock(ContactRepository.class), mock(CompanyMapper.class), addressMapper);

    var addressDto = TestUtils.createUpdatebleAddressDto(ADDRESS_ID);
    addressService.updateAddress(ID, addressDto);

    verify(addressMapper).updateEntityFromDto(addressDto, address);
    verify(addressRepository).save(address);
  }

  @Test
  void testFindAll() {
    var company = TestUtils.createCompany(ID);
    var companyRepository = mock(CompanyRepository.class);
    when(companyRepository.findAll(ArgumentMatchers.<Example<Company>>any(), any(Pageable.class)))
        .thenReturn(new PageImpl<Company>(List.of(company)));
    var expected = new CompanyDto();
    var companyMapper = mock(CompanyMapper.class);
    when(companyMapper.toDto(any(Company.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var companyService = new CompanyServiceImpl(companyRepository, mock(AddressRepository.class),
        mock(ContactRepository.class), companyMapper, mock(AddressMapper.class));
    var page = companyService.findAll(null, pageable);

    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var companyService =
        new CompanyServiceImpl(mock(CompanyRepository.class), mock(AddressRepository.class),
            mock(ContactRepository.class), mock(CompanyMapper.class), mock(AddressMapper.class));
    var companyOpt = companyService.findOne(ID);
    assertThat(companyOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var company = TestUtils.createCompany(ID);
    var companyRepository = mock(CompanyRepository.class);
    when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
    var expected = new CompanyDto();
    var companyMapper = mock(CompanyMapper.class);
    when(companyMapper.toDto(any(Company.class))).thenReturn(expected);

    var companyService = new CompanyServiceImpl(companyRepository, mock(AddressRepository.class),
        mock(ContactRepository.class), companyMapper, mock(AddressMapper.class));
    var companyOpt = companyService.findOne(ID);
    assertThat(companyOpt).isPresent();
    var actual = companyOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var companyRepository = mock(CompanyRepository.class);
    var addressRepository = mock(AddressRepository.class);
    var contactRepository = mock(ContactRepository.class);
    var companyService = new CompanyServiceImpl(companyRepository, addressRepository,
        contactRepository, mock(CompanyMapper.class), mock(AddressMapper.class));
    companyService.delete(ID);
    verify(companyRepository, times(0)).deleteById(ID);

    var address = TestUtils.createAddress(ADDRESS_ID);
    var contact = TestUtils.createContact(CONTACT_ID);
    address.getContacts().add(contact);
    var company = TestUtils.createCompany(ID);
    company.getAddresses().add(address);
    when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
    companyService.delete(ID);
    verify(addressRepository).delete(address);
    verify(contactRepository).delete(contact);
    verify(companyRepository).deleteById(ID);
  }

  @Test
  void testAddAddress() {
    var company = TestUtils.createCompany(ID);
    var companyRepository = mock(CompanyRepository.class);
    when(companyRepository.findById(ID)).thenReturn(Optional.of(company));

    var address = TestUtils.createAddress(ADDRESS_ID);
    var addressMapper = mock(AddressMapper.class);
    when(addressMapper.toEntity(any(NewAddressDto.class))).thenReturn(address);

    var addressRepository = mock(AddressRepository.class);
    when(addressRepository.save(any(Address.class))).thenReturn(address);

    var expected = TestUtils.createAddressDto(ADDRESS_ID);
    when(addressMapper.toDto(any(Address.class))).thenReturn(expected);

    var addressDto = TestUtils.createNewAddressDto();
    var companyService = new CompanyServiceImpl(companyRepository, addressRepository,
        mock(ContactRepository.class), mock(CompanyMapper.class), addressMapper);
    var actual = companyService.addAddress(ID, addressDto);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testFindAllAddress() {
    var addressRepository = mock(AddressRepository.class);
    var address = TestUtils.createAddress(ADDRESS_ID);
    address.setCompany(TestUtils.createCompany(ID));
    when(addressRepository.findAll(ArgumentMatchers.<Example<Address>>any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(address)));
    var addressMapper = mock(AddressMapper.class);
    var expected = new AddressDto();
    when(addressMapper.toDto(any(Address.class))).thenReturn(expected);
    var companyService = new CompanyServiceImpl(mock(CompanyRepository.class), addressRepository,
        mock(ContactRepository.class), mock(CompanyMapper.class), addressMapper);
    var pageable = PageRequest.of(1, 1);
    var page = companyService.findAllAddress(ID, pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testDeleteAddress() {
    var address = TestUtils.createAddress(ADDRESS_ID);
    address.setCompany(TestUtils.createCompany(ID));
    var addressRepository = mock(AddressRepository.class);
    when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(address));

    var companyService = new CompanyServiceImpl(mock(CompanyRepository.class), addressRepository,
        mock(ContactRepository.class), mock(CompanyMapper.class), mock(AddressMapper.class));
    companyService.deleteAddress(ID, ADDRESS_ID);
    verify(addressRepository).delete(address);
  }
}
