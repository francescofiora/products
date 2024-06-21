package it.francescofiora.product.company.api.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.api.util.TestUtils;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.BaseAddressDto;
import it.francescofiora.product.company.dto.BaseCompanyDto;
import it.francescofiora.product.company.dto.CompanyDto;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class CompanyApiEndToEndTest extends AbstractTestEndToEnd {

  public static final String COMPANIES_URI = "/api/v1/companies";
  public static final String COMPANIES_ID_URI = "/api/v1/companies/%d";
  public static final String ADDRESS_ID_URI = "/api/v1/companies/%d/addresses/%d";

  public static final String ALERT_CREATED = "CompanyDto.created";
  private static final String ALERT_UPDATED = "CompanyDto.updated";
  private static final String ALERT_DELETED = "CompanyDto.deleted";
  private static final String ADDRESS_ALERT_DELETED = "AddressDto.deleted";
  public static final String ALERT_GET = "CompanyDto.get";
  private static final String ALERT_BAD_REQUEST = "CompanyDto.badRequest";
  private static final String ALERT_NOT_FOUND = "CompanyDto.notFound";
  private static final String ADDRESS_ALERT_NOT_FOUND = "AddressDto.notFound";

  private static final String ALERT_CREATE_BAD_REQUEST = "NewCompanyDto.badRequest";
  private static final String ALERT_UPDATE_BAD_REQUEST = "UpdatebleCompanyDto.badRequest";

  private static final String NEW_COMP_NAME_NOT_BLANK = "[newCompanyDto.name - NotBlank]";
  private static final String NEW_COMP_EMAIL_NOT_BLANK = "[newCompanyDto.email - NotBlank]";
  private static final String NEW_COMP_WEB_NOT_BLANK = "[newCompanyDto.web - NotBlank]";

  private static final String UP_COMP_NAME_NOT_BLANK = "[updatebleCompanyDto.name - NotBlank]";
  private static final String UP_COMP_EMAIL_NOT_BLANK = "[updatebleCompanyDto.email - NotBlank]";
  private static final String UP_COMP_WEB_NOT_BLANK = "[updatebleCompanyDto.web - NotBlank]";

  private static final String PARAM_ADDRESS_NOT_BLANK =
      "[newCompanyDto.addresses[0].address - NotBlank]";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  @Test
  void testCreate() {
    var newCompanyDto = TestUtils.createNewCompanyDto();
    var newAddressDto = TestUtils.createNewAddressDto();
    newCompanyDto.getAddresses().add(newAddressDto);
    var companyId = createAndReturnId(COMPANIES_URI, newCompanyDto, ALERT_CREATED);

    var companyIdUri = String.format(COMPANIES_ID_URI, companyId);

    var actualDto = get(companyIdUri, CompanyDto.class, ALERT_GET, String.valueOf(companyId));
    assertIsEqualTo(actualDto, newCompanyDto);
    assertThat(actualDto.getAddresses()).hasSize(1);
    var actualAddress = actualDto.getAddresses().get(0);
    assertIsEqualTo(actualAddress, newAddressDto);

    var companyDto = TestUtils.createUpdatebleCompanyDto(companyId);
    update(companyIdUri, companyDto, ALERT_UPDATED, String.valueOf(companyId));

    var addressDto = TestUtils.createUpdatebleAddressDto(actualAddress.getId());
    var addressIdUri = String.format(ADDRESS_ID_URI, companyId, addressDto.getId());
    update(addressIdUri, addressDto, ALERT_UPDATED, String.valueOf(addressDto.getId()));

    actualDto = get(companyIdUri, CompanyDto.class, ALERT_GET, String.valueOf(companyId));
    assertIsEqualTo(actualDto, companyDto);
    actualAddress = actualDto.getAddresses().get(0);
    assertIsEqualTo(actualAddress, addressDto);

    delete(addressIdUri, ADDRESS_ALERT_DELETED, String.valueOf(addressDto.getId()));

    assertGetNotFound(addressIdUri, AddressDto.class, ADDRESS_ALERT_NOT_FOUND,
        String.valueOf(addressDto.getId()));

    var companies =
        get(COMPANIES_URI, PageRequest.of(1, 1), CompanyDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(companies).isNotEmpty();
    var option =
        Stream.of(companies).filter(company -> company.getId().equals(companyId)).findAny();
    assertThat(option).isPresent().contains(actualDto);

    delete(companyIdUri, ALERT_DELETED, String.valueOf(companyId));

    assertGetNotFound(companyIdUri, CompanyDto.class, ALERT_NOT_FOUND, String.valueOf(companyId));
  }

  private void assertIsEqualTo(CompanyDto actualDto, BaseCompanyDto companyDto) {
    assertThat(actualDto.getName()).isEqualTo(companyDto.getName());
    assertThat(actualDto.getEmail()).isEqualTo(companyDto.getEmail());
    assertThat(actualDto.getWeb()).isEqualTo(companyDto.getWeb());
  }

  private void assertIsEqualTo(AddressDto actualDto, BaseAddressDto addressDto) {
    assertThat(actualDto.getAddress()).isEqualTo(addressDto.getAddress());
    assertThat(actualDto.getCountry()).isEqualTo(addressDto.getCountry());
    assertThat(actualDto.getCurrency()).isEqualTo(addressDto.getCurrency());
    assertThat(actualDto.getEmail()).isEqualTo(addressDto.getEmail());
    assertThat(actualDto.getPhone()).isEqualTo(addressDto.getPhone());
    assertThat(actualDto.getTaxNumber()).isEqualTo(addressDto.getTaxNumber());
    assertThat(actualDto.getType()).isEqualTo(addressDto.getType());
    assertThat(actualDto.getZipcode()).isEqualTo(addressDto.getZipcode());
  }

  @Test
  void testCreateBadRequest() {
    var newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.setName(null);
    assertCreateBadRequest(COMPANIES_URI, newCompanyDto, ALERT_CREATE_BAD_REQUEST,
        NEW_COMP_NAME_NOT_BLANK);

    newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.setEmail(null);
    assertCreateBadRequest(COMPANIES_URI, newCompanyDto, ALERT_CREATE_BAD_REQUEST,
        NEW_COMP_EMAIL_NOT_BLANK);

    newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.setWeb(null);
    assertCreateBadRequest(COMPANIES_URI, newCompanyDto, ALERT_CREATE_BAD_REQUEST,
        NEW_COMP_WEB_NOT_BLANK);

    var newAddressDto = TestUtils.createNewAddressDto();
    newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.getAddresses().add(newAddressDto);
    newAddressDto.setAddress(null);
    assertCreateBadRequest(COMPANIES_URI, newCompanyDto, ALERT_CREATE_BAD_REQUEST,
        PARAM_ADDRESS_NOT_BLANK);
  }

  @Test
  void testUpdateBadRequest() {
    Long companyId = 100L;
    var companyDto = TestUtils.createUpdatebleCompanyDto(companyId);
    var companyIdUri = String.format(COMPANIES_ID_URI, companyId);
    assertUpdateNotFound(companyIdUri, companyDto, ALERT_NOT_FOUND, companyId.toString());

    companyIdUri = String.format(COMPANIES_ID_URI, 2L);
    assertUpdateBadRequest(companyIdUri, companyDto, ALERT_BAD_REQUEST, companyId.toString());

    var newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.getAddresses().add(TestUtils.createNewAddressDto());
    companyId = createAndReturnId(COMPANIES_URI, newCompanyDto, ALERT_CREATED);

    companyIdUri = String.format(COMPANIES_ID_URI, companyId);

    companyDto = TestUtils.createUpdatebleCompanyDto(companyId);
    companyDto.setName(null);
    assertUpdateBadRequest(companyIdUri, companyDto, ALERT_UPDATE_BAD_REQUEST,
        UP_COMP_NAME_NOT_BLANK);

    companyDto = TestUtils.createUpdatebleCompanyDto(companyId);
    companyDto.setEmail(null);
    assertUpdateBadRequest(companyIdUri, companyDto, ALERT_UPDATE_BAD_REQUEST,
        UP_COMP_EMAIL_NOT_BLANK);

    companyDto = TestUtils.createUpdatebleCompanyDto(companyId);
    companyDto.setWeb(null);
    assertUpdateBadRequest(companyIdUri, companyDto, ALERT_UPDATE_BAD_REQUEST,
        UP_COMP_WEB_NOT_BLANK);
  }

  @Test
  void testGetBadRequest() {
    assertGetBadRequest(COMPANIES_URI + "/999999999999999999999999", String.class, "id.badRequest",
        PARAM_NOT_VALID_LONG);
  }
}
