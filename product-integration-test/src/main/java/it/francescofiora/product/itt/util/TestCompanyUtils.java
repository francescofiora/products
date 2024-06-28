package it.francescofiora.product.itt.util;

import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import it.francescofiora.product.company.dto.enumeration.AddressType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility for testing company Application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestCompanyUtils {

  /**
   * Create a new NewCompanyDto.
   *
   * @param companyName the name of the company
   * @param companyEmail the email of the company
   * @param companyWeb the of the company
   * @param addressType the type of the address
   * @param addressName the name of the address
   * @param addressZipcode the zip code of the address
   * @param addressCountry the country of the address
   * @param addressCurrency the currency of the address
   * @param addressTaxNumber the tax number of the address
   * @param addressPhone the phone of the address
   * @param addressEmail  the email of the address
   * @return the NewCompanyDto
   */
  public static NewCompanyDto createNewCompanyDto(String companyName, String companyEmail,
      String companyWeb, String addressType, String addressName,
      String addressZipcode, String addressCountry, String addressCurrency, String addressTaxNumber,
      String addressPhone, String addressEmail) {
    var company = new NewCompanyDto();
    company.setName(companyName);
    company.setEmail(companyEmail);
    company.setWeb(companyWeb);
    company.getAddresses().add(createNewAddressDto(addressType, addressName, addressZipcode,
        addressCountry, addressCurrency, addressTaxNumber, addressPhone, addressEmail));
    return company;
  }

  /**
   * Create a new NewAddressDto.
   *
   * @param addressType the type of the address
   * @param addressName the name of the address
   * @param addressZipcode the zip code of the address
   * @param addressCountry the country of the address
   * @param addressCurrency the currency of the address
   * @param addressTaxNumber the tax number of the address
   * @param addressPhone the phone of the address
   * @param addressEmail  the email of the address
   * @return the NewAddressDto
   */
  public static NewAddressDto createNewAddressDto(String addressType, String addressName,
      String addressZipcode, String addressCountry, String addressCurrency, String addressTaxNumber,
      String addressPhone, String addressEmail) {
    var address = new NewAddressDto();
    address.setType(AddressType.valueOf(addressType));
    address.setAddress(addressName);
    address.setZipcode(addressZipcode);
    address.setCountry(addressCountry);
    address.setCurrency(addressCurrency);
    address.setTaxNumber(addressTaxNumber);
    address.setPhone(addressPhone);
    address.setEmail(addressEmail);
    return address;
  }

  /**
   * Create a new UpdatebleCompanyDto.
   *
   * @param companyId the id of the company
   * @param companyName the name of the company
   * @param companyEmail the email of the company
   * @param companyWeb the of the company
   * @return the UpdatebleCompanyDto
   */
  public static UpdatebleCompanyDto createUpdatebleCompanyDto(Long companyId, String companyName,
      String companyEmail, String companyWeb) {
    var company = new UpdatebleCompanyDto();
    company.setId(companyId);
    company.setName(companyName);
    company.setEmail(companyEmail);
    company.setWeb(companyWeb);
    return company;
  }

  /**
   * Create a new UpdatebleAddressDto.
   *
   * @param addressId the id of the address
   * @param addressType the type of the address
   * @param addressName the name of the address
   * @param addressZipcode the zip code of the address
   * @param addressCountry the country of the address
   * @param addressCurrency the currency of the address
   * @param addressTaxNumber the tax number of the address
   * @param addressPhone the phone of the address
   * @param addressEmail  the email of the address
   * @return the NewAddressDto
   */
  public static UpdatebleAddressDto createUpdatebleAddressDto(Long addressId, String addressType,
      String addressName, String addressZipcode, String addressCountry, String addressCurrency,
      String addressTaxNumber, String addressPhone, String addressEmail) {
    var address = new UpdatebleAddressDto();
    address.setId(addressId);
    address.setType(AddressType.valueOf(addressType));
    address.setAddress(addressName);
    address.setZipcode(addressZipcode);
    address.setCountry(addressCountry);
    address.setCurrency(addressCurrency);
    address.setTaxNumber(addressTaxNumber);
    address.setPhone(addressPhone);
    address.setEmail(addressEmail);
    return address;
  }
}
