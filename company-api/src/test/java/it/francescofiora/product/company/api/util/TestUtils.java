package it.francescofiora.product.company.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.common.domain.DomainIdentifier;
import it.francescofiora.product.company.api.domain.Address;
import it.francescofiora.product.company.api.domain.Company;
import it.francescofiora.product.company.api.domain.Contact;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.RefAddressDto;
import it.francescofiora.product.company.dto.RefCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleContactDto;
import it.francescofiora.product.company.dto.enumeration.AddressType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility for testing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  /**
   * Create an example of Contact.
   *
   * @param id the id
   * @return Contact
   */
  public static Contact createContact(Long id) {
    var contact = new Contact();
    contact.setId(id);
    contact.setName("Name");
    contact.setDescription("Description");
    contact.setPhone("Phone");
    contact.setEmail("Email");
    return contact;
  }

  /**
   * Create an example of Address.
   *
   * @param id the id
   * @return Address
   */
  public static Address createAddress(Long id) {
    var address = new Address();
    address.setId(id);
    address.setType(AddressType.HEADQUARTER);
    address.setAddress("Address");
    address.setZipcode("Zipcode");
    address.setCountry("Country");
    address.setCurrency("USD");
    address.setTaxNumber("1234");
    address.setPhone("555");
    address.setEmail("test@mail");
    return address;
  }

  /**
   * Create an example of Company.
   *
   * @param id the id
   * @return Company
   */
  public static Company createCompany(Long id) {
    var company = new Company();
    company.setId(id);
    company.setName("Name");
    company.setEmail("Email");
    company.setWeb("Web");
    return company;
  }

  /**
   * Create an example of NewContactDto.
   *
   * @return NewContactDto
   */
  public static NewContactDto createNewContactDto() {
    var contactDto = new NewContactDto();
    contactDto.setName("Name");
    contactDto.setDescription("Description");
    contactDto.setPhone("Phone");
    contactDto.setEmail("Email");
    return contactDto;
  }

  /**
   * Create an example of ContactDto.
   *
   * @param id the id
   * @return ContactDto
   */
  public static ContactDto createContactDto(Long id) {
    var contactDto = new ContactDto();
    contactDto.setId(id);
    contactDto.setName("Name");
    contactDto.setDescription("Description");
    contactDto.setPhone("Phone");
    contactDto.setEmail("Email");
    return contactDto;
  }

  /**
   * Create an example of UpdatebleContactDto.
   *
   * @param id the id
   * @return UpdatebleContactDto
   */
  public static UpdatebleContactDto createUpdatebleContactDto(Long id) {
    var contactDto = new UpdatebleContactDto();
    contactDto.setId(id);
    contactDto.setName("Name updated");
    contactDto.setDescription("Description updated");
    contactDto.setPhone("Phone updated");
    contactDto.setEmail("Email updated");
    return contactDto;
  }

  /**
   * Create an example of NewAddressDto.
   *
   * @return NewAddressDto
   */
  public static NewAddressDto createNewAddressDto() {
    var address = new NewAddressDto();
    address.setType(AddressType.HEADQUARTER);
    address.setAddress("Address");
    address.setZipcode("Zipcode");
    address.setCountry("Country");
    address.setCurrency("USD");
    address.setTaxNumber("1234");
    address.setPhone("555");
    address.setEmail("test@mail");
    return address;
  }

  /**
   * Create an example of AddressDto.
   *
   * @param id the id
   * @return AddressDto
   */
  public static AddressDto createAddressDto(Long id) {
    var address = new AddressDto();
    address.setId(id);
    address.setType(AddressType.HEADQUARTER);
    address.setAddress("Address");
    address.setZipcode("Zipcode");
    address.setCountry("Country");
    address.setCurrency("USD");
    address.setTaxNumber("1234");
    address.setPhone("555");
    address.setEmail("test@mail");
    return address;
  }

  /**
   * Create an example of UpdatebleAddressDto.
   *
   * @param id the id of the Address
   * @return the UpdatebleAddressDto
   */
  public static UpdatebleAddressDto createUpdatebleAddressDto(Long id) {
    var addressDto = new UpdatebleAddressDto();
    addressDto.setId(id);
    addressDto.setType(AddressType.BRANCH);
    addressDto.setAddress("Address updated");
    addressDto.setZipcode("Zipcode updated");
    addressDto.setCountry("Country updated");
    addressDto.setCurrency("EUR");
    addressDto.setTaxNumber("4321");
    addressDto.setPhone("555 - 555");
    addressDto.setEmail("test@mail.org");
    return addressDto;
  }

  /**
   * Create an example of NewCompanyDto.
   *
   * @return NewCompanyDto
   */
  public static NewCompanyDto createNewCompanyDto() {
    var companyDto = new NewCompanyDto();
    companyDto.setName("Name");
    companyDto.setEmail("mail@test");
    companyDto.setWeb("www");
    return companyDto;
  }

  /**
   * Create an example of CompanyDto.
   *
   * @param id the id
   * @return CompanyDto
   */
  public static CompanyDto createCompanyDto(Long id) {
    var companyDto = new CompanyDto();
    companyDto.setId(id);
    companyDto.setName("Name");
    companyDto.setEmail("mail@test");
    companyDto.setWeb("www");
    return companyDto;
  }

  /**
   * Create an example of UpdatebleCompanyDto.
   *
   * @param id the id
   * @return UpdatebleCompanyDto
   */
  public static UpdatebleCompanyDto createUpdatebleCompanyDto(Long id) {
    var companyDto = new UpdatebleCompanyDto();
    companyDto.setId(id);
    companyDto.setName("Name updated");
    companyDto.setEmail("mail@test.org");
    companyDto.setWeb("www.name");
    return companyDto;
  }

  /**
   * Create RefCompanyDto.
   *
   * @param id the id of the Company
   * @return RefCompanyDto
   */
  public static RefCompanyDto createRefCompanyDto(Long id) {
    var company = new RefCompanyDto();
    company.setId(id);
    return company;
  }

  /**
   * Create RefAddressDto.
   *
   * @param id the id of the Address
   * @return RefAddressDto
   */
  public static RefAddressDto createRefAddressDto(Long id) {
    var address = new RefAddressDto();
    address.setId(id);
    return address;
  }

  /**
   * Create new DomainIdentifier.
   *
   * @param clazz the DomainIdentifier class.
   * @param id the id
   * @return a new DomainIdentifier Object
   * @throws Exception if error occurs
   */
  public static <T> Object createNewDomain(Class<T> clazz, Long id) throws Exception {
    var domainObj = (DomainIdentifier) clazz.getConstructor().newInstance();
    domainObj.setId(id);
    return domainObj;
  }

  /**
   * Assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1).isEqualTo(obj2);
    assertThat(obj1).hasSameHashCodeAs(obj2.hashCode());
    assertThat(obj1).hasToString(obj2.toString());
  }

  /**
   * Assert that obj1 is not equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1).isNotEqualTo(obj2);
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }
}
