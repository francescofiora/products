package it.francescofiora.product.company.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.RefAddressDto;
import org.junit.jupiter.api.Test;

class AddressMapperTest {

  @Test
  void testNullObject() {
    var addressMapper = new AddressMapperImpl();
    assertThat(addressMapper.toDto(null)).isNull();

    var addressDto = (NewAddressDto) null;
    assertThat(addressMapper.toEntity(addressDto)).isNull();

    var dto = (RefAddressDto) null;
    assertThat(addressMapper.toEntity(dto)).isNull();

    assertDoesNotThrow(() -> addressMapper.updateEntityFromDto(null, null));
  }
}
