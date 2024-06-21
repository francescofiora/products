package it.francescofiora.product.company.api.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class CompanyMapperTest {

  @Test
  void testNullObject() {
    var companyMapper = new CompanyMapperImpl();
    assertThat(companyMapper.toDto(null)).isNull();
    assertThat(companyMapper.toEntity(null)).isNull();
    assertThat(companyMapper.addressSetToAddressDtoList(null)).isNull();
    assertThat(companyMapper.newAddressDtoListToAddressSet(null)).isNull();

    assertDoesNotThrow(() -> companyMapper.updateEntityFromDto(null, null));
  }
}
