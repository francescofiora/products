package it.francescofiora.product.company.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class ContactMapperTest {

  @Test
  void testNullObject() {
    var contactMapper = new ContactMapperImpl();
    assertThat(contactMapper.toDto(null)).isNull();
    assertThat(contactMapper.toEntity(null)).isNull();
    assertThat(contactMapper.addressToRefCompanyDto(null)).isNull();
    assertThat(contactMapper.addressToRefAddressDto(null)).isNull();

    assertDoesNotThrow(() -> contactMapper.updateEntityFromDto(null, null));
  }
}
