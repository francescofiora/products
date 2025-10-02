package it.francescofiora.product.itt.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.francescofiora.product.company.client.CompanyApiService;
import it.francescofiora.product.itt.context.CompanyContext;
import it.francescofiora.product.itt.util.TestCompanyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Company Component.
 */
@Component
@RequiredArgsConstructor
public class CompanyComponent extends AbstractComponent {

  private final CompanyContext companyContext;
  private final CompanyApiService companyApiService;

  /**
   * Create a new NewCompanyDto and save into the context.
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
   */
  public void createNewCompanyDto(String companyName, String companyEmail,
      String companyWeb, String addressType, String addressName,
      String addressZipcode, String addressCountry, String addressCurrency, String addressTaxNumber,
      String addressPhone, String addressEmail) {
    companyContext.setNewCompanyDto(TestCompanyUtils.createNewCompanyDto(companyName, companyEmail,
        companyWeb, addressType, addressName, addressZipcode, addressCountry, addressCurrency,
        addressTaxNumber, addressPhone, addressEmail));
  }

  /**
   * Create a new NewAddressDto and save into the context.
   *
   * @param addressType the type of the address
   * @param addressName the name of the address
   * @param addressZipcode the zip code of the address
   * @param addressCountry the country of the address
   * @param addressCurrency the currency of the address
   * @param addressTaxNumber the tax number of the address
   * @param addressPhone the phone of the address
   * @param addressEmail  the email of the address
   */
  public void createNewAddressDto(String addressType, String addressName, String addressZipcode,
      String addressCountry, String addressCurrency, String addressTaxNumber,
      String addressPhone, String addressEmail) {
    companyContext.setNewAddressDto(TestCompanyUtils.createNewAddressDto(addressType, addressName,
        addressZipcode, addressCountry, addressCurrency, addressTaxNumber,
        addressPhone, addressEmail));
  }

  public void createCompany() {
    var result = companyApiService.createCompany(companyContext.getNewCompanyDto());
    companyContext.setCompanyId(validateResponseAndGetId(result));
  }

  /**
   * Create Address.
   */
  public void createAddress() {
    var result = companyApiService
        .addAddress(companyContext.getCompanyId(), companyContext.getNewAddressDto());
    companyContext.setAddressId(validateResponseAndGetId(result));
  }

  /**
   * Fetch Company by id and save into the context.
   */
  public void fetchCompany() {
    var result = companyApiService.getCompanyById(companyContext.getCompanyId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    companyContext.setCompanyDto(result.getBody());
  }

  /**
   * Fetch Address by id and save into the context.
   */
  public void fetchAddress() {
    var result = companyApiService
        .getAddressById(companyContext.getCompanyId(), companyContext.getAddressId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    companyContext.setAddressDto(result.getBody());
  }

  /**
   * Compare Company with NewCompany.
   */
  public void compareCompanyWithNewCompany() {
    var company = companyContext.getCompanyDto();
    assertNotNull(company);
    var newCompanyDto = companyContext.getNewCompanyDto();
    assertNotNull(newCompanyDto);
    assertThat(company.getName()).isEqualTo(newCompanyDto.getName());
    assertThat(company.getEmail()).isEqualTo(newCompanyDto.getEmail());
    assertThat(company.getWeb()).isEqualTo(newCompanyDto.getWeb());
  }

  /**
   * Compare Address with NewAddress.
   */
  public void compareAddressWithNewAddress() {
    var address = companyContext.getAddressDto();
    assertNotNull(address);
    var newAddressDto = companyContext.getNewAddressDto();
    assertNotNull(newAddressDto);
    assertThat(address.getType()).isEqualTo(newAddressDto.getType());
    assertThat(address.getAddress()).isEqualTo(newAddressDto.getAddress());
    assertThat(address.getZipcode()).isEqualTo(newAddressDto.getZipcode());
    assertThat(address.getCountry()).isEqualTo(newAddressDto.getCountry());
    assertThat(address.getCurrency()).isEqualTo(newAddressDto.getCurrency());
    assertThat(address.getTaxNumber()).isEqualTo(newAddressDto.getTaxNumber());
    assertThat(address.getPhone()).isEqualTo(newAddressDto.getPhone());
    assertThat(address.getEmail()).isEqualTo(newAddressDto.getEmail());
  }

  /**
   * Update Company.
   *
   * @param companyName the name of the company
   * @param companyEmail the email of the company
   * @param companyWeb the of the company
   */
  public void updateCompany(String companyName, String companyEmail, String companyWeb) {
    var companyId = companyContext.getCompanyId();
    companyContext.setUpdatebleCompanyDto(TestCompanyUtils
        .createUpdatebleCompanyDto(companyId, companyName, companyEmail, companyWeb));
    var result = companyApiService
        .updateCompany(companyContext.getCompanyId(), companyContext.getUpdatebleCompanyDto());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /**
   * Update Address.
   *
   * @param addressType the type of the address
   * @param addressName the name of the address
   * @param addressZipcode the zip code of the address
   * @param addressCountry the country of the address
   * @param addressCurrency the currency of the address
   * @param addressTaxNumber the tax number of the address
   * @param addressPhone the phone of the address
   * @param addressEmail  the email of the address
   */
  public void updateAddress(String addressType, String addressName, String addressZipcode,
      String addressCountry, String addressCurrency, String addressTaxNumber,
      String addressPhone, String addressEmail) {
    var addressId = companyContext.getAddressId();
    companyContext.setUpdatebleAddressDto(TestCompanyUtils.createUpdatebleAddressDto(addressId,
        addressType, addressName, addressZipcode, addressCountry, addressCurrency, addressTaxNumber,
        addressPhone, addressEmail));
    var result = companyApiService.updateAddress(companyContext.getCompanyId(), addressId,
        companyContext.getUpdatebleAddressDto());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /**
   * Fetch all Companies.
   */
  public void fetchAllCompanies() {
    var result = companyApiService.findCompanies(null, Pageable.unpaged());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotEmpty();
    companyContext.setCompanies(result.getBody());
  }

  /**
   * Fetch all Addresses.
   */
  public void fetchAllAddresses() {
    var result = companyApiService
        .findAddresses(companyContext.getCompanyId(), Pageable.unpaged());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotEmpty();
    companyContext.setAddresses(result.getBody());
  }

  /**
   * Compare UpdatebleCompany into Companies.
   */
  public void compareUpdatebleCompanyIntoCompanies() {
    var companyId = companyContext.getCompanyId();
    var opt = companyContext.getCompanies().stream()
        .filter(cmp -> companyId.equals(cmp.getId())).findAny();
    assertThat(opt).isNotEmpty();
    var company = opt.get();
    var updatebleCompany = companyContext.getUpdatebleCompanyDto();
    assertThat(company.getName()).isEqualTo(updatebleCompany.getName());
    assertThat(company.getEmail()).isEqualTo(updatebleCompany.getEmail());
    assertThat(company.getWeb()).isEqualTo(updatebleCompany.getWeb());
  }

  /**
   * compare UpdatebleAddress into Addresses.
   */
  public void compareUpdatebleAddressIntoAddresses() {
    var addressId = companyContext.getAddressId();
    var opt = companyContext.getAddresses().stream()
        .filter(adr -> addressId.equals(adr.getId())).findAny();
    assertThat(opt).isNotEmpty();
    var address = opt.get();
    var updatebleAddress = companyContext.getUpdatebleAddressDto();
    assertThat(address.getType()).isEqualTo(updatebleAddress.getType());
    assertThat(address.getAddress()).isEqualTo(updatebleAddress.getAddress());
    assertThat(address.getZipcode()).isEqualTo(updatebleAddress.getZipcode());
    assertThat(address.getCountry()).isEqualTo(updatebleAddress.getCountry());
    assertThat(address.getCurrency()).isEqualTo(updatebleAddress.getCurrency());
    assertThat(address.getTaxNumber()).isEqualTo(updatebleAddress.getTaxNumber());
    assertThat(address.getPhone()).isEqualTo(updatebleAddress.getPhone());
    assertThat(address.getEmail()).isEqualTo(updatebleAddress.getEmail());
  }

  public void deleteCompany() {
    var result = companyApiService.deleteCompanyById(companyContext.getCompanyId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Delete Address.
   */
  public void deleteAddress() {
    var result = companyApiService
        .deleteAddressById(companyContext.getCompanyId(), companyContext.getAddressId());
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  /**
   * Check Company Not Exist.
   */
  public void checkCompanyNotExist() {
    var result = companyApiService.findCompanies(null, Pageable.unpaged());
    assertThat(result.getBody()).isNotNull();
    companyContext.setCompanies(result.getBody());
    var companyId = companyContext.getCompanyId();
    var opt = companyContext.getCompanies().stream()
        .filter(cmp -> companyId.equals(cmp.getId())).findAny();
    assertThat(opt).isEmpty();
  }

  /**
   * Check Address Not Exist.
   */
  public void checkAddressNotExist() {
    var result = companyApiService.findAddresses(companyContext.getCompanyId(), Pageable.unpaged());
    assertThat(result.getBody()).isNotNull();
    companyContext.setAddresses(result.getBody());
    var addressId = companyContext.getAddressId();
    var opt = companyContext.getAddresses().stream()
        .filter(adr -> addressId.equals(adr.getId())).findAny();
    assertThat(opt).isEmpty();
  }
}
