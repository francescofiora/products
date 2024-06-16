package it.francescofiora.product.company.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.util.TestUtils;
import org.junit.jupiter.api.Test;

class NewContactDtoTest {

  @Test
  void dtoEqualsVerifier() {
    var contactDto1 = TestUtils.createNewContactDto();
    var contactDto2 = new NewContactDto();
    assertThat(contactDto1).isNotEqualTo(contactDto2);

    contactDto2 = TestUtils.createNewContactDto();
    TestUtils.checkEqualHashAndToString(contactDto1, contactDto2);

    contactDto2.setName("NameDiff");
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);
    contactDto1.setName(null);
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);

    contactDto1 = TestUtils.createNewContactDto();
    contactDto2 = TestUtils.createNewContactDto();
    contactDto2.setDescription("DescriptionDiff");
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);
    contactDto1.setDescription(null);
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);

    contactDto1 = TestUtils.createNewContactDto();
    contactDto2 = TestUtils.createNewContactDto();
    contactDto2.setEmail("EmailDiff");
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);
    contactDto1.setEmail(null);
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);

    contactDto1 = TestUtils.createNewContactDto();
    contactDto2 = TestUtils.createNewContactDto();
    contactDto2.setPhone("(554) 5");
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);
    contactDto1.setPhone(null);
    TestUtils.checkNotEqualHashAndToString(contactDto1, contactDto2);
  }
}
