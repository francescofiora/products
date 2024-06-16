package it.francescofiora.product.company.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.common.dto.DtoIdentifier;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.enumeration.AddressType;

/**
 * Utility for testing.
 */
public interface TestUtils {

  /**
   * Create an example of NewAddressDto.
   *
   * @return NewAddressDto
   */
  static NewAddressDto createNewAddressDto() {
    var addressDto = new NewAddressDto();
    addressDto.setType(AddressType.HEADQUARTER);
    addressDto.setAddress("Address");
    addressDto.setZipcode("1234");
    addressDto.setCountry("Country");
    addressDto.setTaxNumber("XX-XX");
    addressDto.setPhone("(555) 555");
    addressDto.setEmail("email@test");
    return addressDto;
  }

  /**
   * Create an example of NewContactDto.
   *
   * @return NewContactDto
   */
  static NewContactDto createNewContactDto() {
    var contactDto = new NewContactDto();
    contactDto.setName("Name");
    contactDto.setDescription("Description");
    contactDto.setPhone("(555)");
    contactDto.setEmail("email@test");
    return contactDto;
  }

  /**
   * Create new DtoIdentifier.
   *
   * @param clazz the DtoIdentifier class.
   * @param id the id
   * @return a new DtoIdentifier
   * @throws Exception if error occurs
   */
  public static <T> DtoIdentifier createNewDtoIdentifier(Class<T> clazz, Long id) throws Exception {
    var dtoObj = (DtoIdentifier) clazz.getConstructor().newInstance();
    dtoObj.setId(id);
    return dtoObj;
  }

  /**
   * assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isTrue();
    assertThat(obj1).hasSameHashCodeAs(obj2.hashCode());
    assertThat(obj1).hasToString(obj2.toString());
  }

  /**
   * assert that obj1 is not equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isFalse();
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }
}
