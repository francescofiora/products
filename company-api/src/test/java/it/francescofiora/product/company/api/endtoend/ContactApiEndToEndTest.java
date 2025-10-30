package it.francescofiora.product.company.api.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.api.util.TestUtils;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewContactDto;
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
class ContactApiEndToEndTest extends AbstractTestEndToEnd {

  private static final String CONTACTS_URI = "/api/v1/contacts";
  private static final String CONTACTS_ID_URI = "/api/v1/contacts/%d";

  private static final String ALERT_CREATED = "ContactDto.created";
  private static final String ALERT_UPDATED = "ContactDto.updated";
  private static final String ALERT_DELETED = "ContactDto.deleted";
  private static final String ALERT_GET = "ContactDto.get";
  private static final String ALERT_NOT_FOUND = "ContactDto.notFound";
  private static final String ALERT_BAD_REQUEST = "ContactDto.badRequest";

  private static final String ALERT_CREATE_BAD_REQUEST = "NewContactDto.badRequest";
  private static final String ALERT_UPDATE_BAD_REQUEST = "UpdatebleContactDto.badRequest";

  private static final String NEW_COMPANY_NOT_NULL = "[newContactDto.company - NotNull]";
  private static final String NEW_ADDRESS_NOT_NULL = "[newContactDto.address - NotNull]";
  private static final String NEW_NAME_NOT_BLANK = "[newContactDto.name - NotBlank]";
  private static final String NEW_DESCRIPTION_NOT_BLANK = "[newContactDto.description - NotBlank]";
  private static final String NEW_EMAIL_NOT_BLANK = "[newContactDto.email - NotBlank]";
  private static final String NEW_PHONE_NOT_BLANK = "[newContactDto.phone - NotBlank]";

  private static final String UP_NAME_NOT_BLANK = "[updatebleContactDto.name - NotBlank]";
  private static final String UP_DESCRIPTION_NOT_BLANK =
      "[updatebleContactDto.description - NotBlank]";
  private static final String UP_EMAIL_NOT_BLANK = "[updatebleContactDto.email - NotBlank]";
  private static final String UP_PHONE_NOT_BLANK = "[updatebleContactDto.phone - NotBlank]";

  private static final String PARAM_PAGE_1 = "0 1";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  @Test
  void testCreate() {
    var companyId = createCompany();
    var addressId = getAdressFromCompanyId(companyId);

    var newContactDto = createNewContactDto(companyId, addressId);
    var contactId = createAndReturnId(CONTACTS_URI, newContactDto, ALERT_CREATED);

    var contactIdUri = String.format(CONTACTS_ID_URI, contactId);

    var actual = get(contactIdUri, ContactDto.class, ALERT_GET, String.valueOf(contactId));
    assertThat(actual.getName()).isEqualTo(newContactDto.getName());
    assertThat(actual.getDescription()).isEqualTo(newContactDto.getDescription());
    assertThat(actual.getEmail()).isEqualTo(newContactDto.getEmail());
    assertThat(actual.getPhone()).isEqualTo(newContactDto.getPhone());
    assertThat(actual.getAddress().getId()).isEqualTo(addressId);
    assertThat(actual.getCompany().getId()).isEqualTo(companyId);

    var contactDto = TestUtils.createUpdatebleContactDto(contactId);
    update(contactIdUri, contactDto, ALERT_UPDATED, String.valueOf(contactId));

    actual = get(contactIdUri, ContactDto.class, ALERT_GET, String.valueOf(contactId));
    assertThat(actual.getName()).isEqualTo(contactDto.getName());
    assertThat(actual.getDescription()).isEqualTo(contactDto.getDescription());
    assertThat(actual.getEmail()).isEqualTo(contactDto.getEmail());
    assertThat(actual.getPhone()).isEqualTo(contactDto.getPhone());

    var contacts =
        get(CONTACTS_URI, PageRequest.of(0, 1), ContactDto[].class, ALERT_GET, PARAM_PAGE_1);
    assertThat(contacts).isNotEmpty();
    var option = Stream.of(contacts).filter(contact -> contact.getId().equals(contactId)).findAny();
    assertThat(option).isPresent().contains(actual);

    delete(contactIdUri, ALERT_DELETED, String.valueOf(contactId));

    assertGetNotFound(contactIdUri, ContactDto.class, ALERT_NOT_FOUND, String.valueOf(contactId));
  }

  private Long createCompany() {
    var newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.getAddresses().add(TestUtils.createNewAddressDto());
    return createAndReturnId(CompanyApiEndToEndTest.COMPANIES_URI, newCompanyDto,
        CompanyApiEndToEndTest.ALERT_CREATED);
  }

  private Long getAdressFromCompanyId(Long companyId) {
    var companyIdUri = String.format(CompanyApiEndToEndTest.COMPANIES_ID_URI, companyId);
    var companyDto = get(companyIdUri, CompanyDto.class, CompanyApiEndToEndTest.ALERT_GET,
        String.valueOf(companyId));
    return companyDto.getAddresses().get(0).getId();
  }

  private NewContactDto createNewContactDto(Long companyId, Long addressId) {
    var newContactDto = TestUtils.createNewContactDto();
    newContactDto.setCompany(TestUtils.createRefCompanyDto(companyId));
    newContactDto.setAddress(TestUtils.createRefAddressDto(addressId));
    return newContactDto;
  }

  @Test
  void testCreateBadRequest() {
    var companyId = createCompany();
    var addressId = getAdressFromCompanyId(companyId);

    var newContactDto = createNewContactDto(companyId, addressId);
    newContactDto.setCompany(null);
    assertCreateBadRequest(CONTACTS_URI, newContactDto, ALERT_CREATE_BAD_REQUEST,
        NEW_COMPANY_NOT_NULL);

    newContactDto = createNewContactDto(companyId, addressId);
    newContactDto.setAddress(null);
    assertCreateBadRequest(CONTACTS_URI, newContactDto, ALERT_CREATE_BAD_REQUEST,
        NEW_ADDRESS_NOT_NULL);

    newContactDto = createNewContactDto(companyId, addressId);
    newContactDto.setName(null);
    assertCreateBadRequest(CONTACTS_URI, newContactDto, ALERT_CREATE_BAD_REQUEST,
        NEW_NAME_NOT_BLANK);

    newContactDto = createNewContactDto(companyId, addressId);
    newContactDto.setDescription(null);
    assertCreateBadRequest(CONTACTS_URI, newContactDto, ALERT_CREATE_BAD_REQUEST,
        NEW_DESCRIPTION_NOT_BLANK);

    newContactDto = createNewContactDto(companyId, addressId);
    newContactDto.setEmail(null);
    assertCreateBadRequest(CONTACTS_URI, newContactDto, ALERT_CREATE_BAD_REQUEST,
        NEW_EMAIL_NOT_BLANK);

    newContactDto = createNewContactDto(companyId, addressId);
    newContactDto.setPhone(null);
    assertCreateBadRequest(CONTACTS_URI, newContactDto, ALERT_CREATE_BAD_REQUEST,
        NEW_PHONE_NOT_BLANK);
  }

  @Test
  void testUpdateBadRequest() {
    Long contactId = 100L;
    var contactDto = TestUtils.createUpdatebleContactDto(contactId);
    var contactIdUri = String.format(CONTACTS_ID_URI, contactId);
    assertUpdateNotFound(contactIdUri, contactDto, ALERT_NOT_FOUND, contactId.toString());

    contactIdUri = String.format(CONTACTS_ID_URI, 2L);
    assertUpdateBadRequest(contactIdUri, contactDto, ALERT_BAD_REQUEST, contactId.toString());

    var companyId = createCompany();
    var addressId = getAdressFromCompanyId(companyId);

    var newContactDto = createNewContactDto(companyId, addressId);
    contactId = createAndReturnId(CONTACTS_URI, newContactDto, ALERT_CREATED);

    contactIdUri = String.format(CONTACTS_ID_URI, contactId);

    contactDto = TestUtils.createUpdatebleContactDto(contactId);
    contactDto.setName(null);
    assertUpdateBadRequest(contactIdUri, contactDto, ALERT_UPDATE_BAD_REQUEST, UP_NAME_NOT_BLANK);

    contactDto = TestUtils.createUpdatebleContactDto(contactId);
    contactDto.setDescription(null);
    assertUpdateBadRequest(contactIdUri, contactDto, ALERT_UPDATE_BAD_REQUEST,
        UP_DESCRIPTION_NOT_BLANK);

    contactDto = TestUtils.createUpdatebleContactDto(contactId);
    contactDto.setEmail(null);
    assertUpdateBadRequest(contactIdUri, contactDto, ALERT_UPDATE_BAD_REQUEST, UP_EMAIL_NOT_BLANK);

    contactDto = TestUtils.createUpdatebleContactDto(contactId);
    contactDto.setPhone(null);
    assertUpdateBadRequest(contactIdUri, contactDto, ALERT_UPDATE_BAD_REQUEST, UP_PHONE_NOT_BLANK);
  }

  @Test
  void testGetBadRequest() {
    assertGetBadRequest(CONTACTS_URI + "/999999999999999999999999", String.class, "id.badRequest",
        PARAM_NOT_VALID_LONG);
  }
}
