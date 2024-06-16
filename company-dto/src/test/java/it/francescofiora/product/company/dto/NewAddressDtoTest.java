package it.francescofiora.product.company.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.dto.enumeration.AddressType;
import it.francescofiora.product.company.util.TestUtils;
import org.junit.jupiter.api.Test;

class NewAddressDtoTest {

  @Test
  void dtoEqualsVerifier() {
    var addressDto1 = TestUtils.createNewAddressDto();
    var addressDto2 = new NewAddressDto();
    assertThat(addressDto1).isNotEqualTo(addressDto2);

    addressDto2 = TestUtils.createNewAddressDto();
    TestUtils.checkEqualHashAndToString(addressDto1, addressDto2);

    addressDto2.setType(AddressType.BRANCH);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setType(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);

    addressDto1 = TestUtils.createNewAddressDto();
    addressDto2 = TestUtils.createNewAddressDto();
    addressDto2.setAddress("AddressDiff");
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setAddress(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);

    addressDto1 = TestUtils.createNewAddressDto();
    addressDto2 = TestUtils.createNewAddressDto();
    addressDto2.setZipcode("ZipcodeDiff");
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setZipcode(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);

    addressDto1 = TestUtils.createNewAddressDto();
    addressDto2 = TestUtils.createNewAddressDto();
    addressDto2.setTaxNumber("TaxNumberDiff");
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setTaxNumber(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);

    addressDto1 = TestUtils.createNewAddressDto();
    addressDto2 = TestUtils.createNewAddressDto();
    addressDto2.setCountry("CountryDiff");
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setCountry(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);

    addressDto1 = TestUtils.createNewAddressDto();
    addressDto2 = TestUtils.createNewAddressDto();
    addressDto2.setCurrency("CurrencyDiff");
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setCurrency(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);

    addressDto1 = TestUtils.createNewAddressDto();
    addressDto2 = TestUtils.createNewAddressDto();
    addressDto2.setEmail("EmailDiff");
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setEmail(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);

    addressDto1 = TestUtils.createNewAddressDto();
    addressDto2 = TestUtils.createNewAddressDto();
    addressDto2.setPhone("(554) 5");
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
    addressDto1.setPhone(null);
    TestUtils.checkNotEqualHashAndToString(addressDto1, addressDto2);
  }
}
