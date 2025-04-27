package it.francescofiora.product.company.api.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.company.api.config.SecurityConfig;
import it.francescofiora.product.company.api.service.CompanyService;
import it.francescofiora.product.company.api.util.TestUtils;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CompanyApi.class)
@Import({BuildProperties.class, SecurityConfig.class})
class CompanyApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final Long ADDRESS_ID = 2L;
  private static final String COMPANIES_URI = "/api/v1/companies";
  private static final String COMPANIES_ID_URI = "/api/v1/companies/{id}";
  private static final String ADDRESS_URI = "/api/v1/companies/{id}/addresses";
  private static final String ADDRESS_ID_URI = "/api/v1/companies/%d/addresses/%d";

  @MockitoBean
  private CompanyService companyService;

  @Test
  void testCreateCompany() throws Exception {
    var newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.getAddresses().add(TestUtils.createNewAddressDto());
    var companyDto = TestUtils.createCompanyDto(ID);
    given(companyService.create(any(NewCompanyDto.class))).willReturn(companyDto);

    var result = performPost(COMPANIES_URI, newCompanyDto).andExpect(status().isCreated())
        .andReturn();
    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(COMPANIES_URI + "/" + ID);
  }

  @Test
  void testCreateCompanyBadRequest() throws Exception {
    var newCompanyDto = TestUtils.createNewCompanyDto();
    performPost(COMPANIES_URI, newCompanyDto).andExpect(status().isBadRequest());

    newCompanyDto = TestUtils.createNewCompanyDto();
    newCompanyDto.getAddresses().add(TestUtils.createNewAddressDto());
    newCompanyDto.setName("");
    performPost(COMPANIES_URI, newCompanyDto).andExpect(status().isBadRequest());

    newCompanyDto.setName(" ");
    performPost(COMPANIES_URI, newCompanyDto).andExpect(status().isBadRequest());

    newCompanyDto.setName(null);
    performPost(COMPANIES_URI, newCompanyDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateCompany() throws Exception {
    var companyDto = TestUtils.createUpdatebleCompanyDto(ID);
    performPut(COMPANIES_ID_URI, ID, companyDto).andExpect(status().isOk());
  }

  @Test
  void testUpdateCompanyBadRequest() throws Exception {
    var companyDto = TestUtils.createUpdatebleCompanyDto(ID);

    companyDto.setName("");
    performPut(COMPANIES_ID_URI, ID, companyDto).andExpect(status().isBadRequest());

    companyDto.setName(" ");
    performPut(COMPANIES_ID_URI, ID, companyDto).andExpect(status().isBadRequest());

    companyDto.setName(null);
    performPut(COMPANIES_ID_URI, ID, companyDto).andExpect(status().isBadRequest());
  }

  @Test
  void testGetAllCompany() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = TestUtils.createCompanyDto(ID);
    given(companyService.findAll(any(), any(Pageable.class)))
        .willReturn(new PageImpl<>(List.of(expected)));

    var result = performGet(COMPANIES_URI, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<CompanyDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetCompany() throws Exception {
    var expected = TestUtils.createCompanyDto(ID);
    given(companyService.findOne(ID)).willReturn(Optional.of(expected));
    var result = performGet(COMPANIES_ID_URI, ID).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<CompanyDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    performDelete(COMPANIES_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testAddAddress() throws Exception {
    var newAddressDto = TestUtils.createNewAddressDto();
    var addressDto = TestUtils.createAddressDto(ADDRESS_ID);
    given(companyService.addAddress(any(), any(NewAddressDto.class))).willReturn(addressDto);

    var result = performPost(ADDRESS_URI, ID, newAddressDto).andExpect(status().isCreated())
        .andReturn();
    var addressIdUri = String.format(ADDRESS_ID_URI, ID, ADDRESS_ID);
    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(addressIdUri);
  }

  @Test
  void testGetAddress() throws Exception {
    var expected = TestUtils.createAddressDto(ADDRESS_ID);
    given(companyService.findOneAddress(ID, ADDRESS_ID)).willReturn(Optional.of(expected));
    var addressIdUri = String.format(ADDRESS_ID_URI, ID, ADDRESS_ID);
    var result = performGet(addressIdUri).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<AddressDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testUpdateAddress() throws Exception {
    var addressDto = TestUtils.createUpdatebleAddressDto(ADDRESS_ID);
    var addressIdUri = String.format(ADDRESS_ID_URI, ID, ADDRESS_ID);
    performPut(addressIdUri, addressDto).andExpect(status().isOk());
  }

  @Test
  void testGetAllAddresses() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = TestUtils.createAddressDto(ADDRESS_ID);
    given(companyService.findAllAddress(any(), any(Pageable.class)))
        .willReturn(new PageImpl<>(List.of(expected)));

    var result = performGet(ADDRESS_URI, ID, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<AddressDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testDeleteAddress() throws Exception {
    var addressIdUri = String.format(ADDRESS_ID_URI, ID, ADDRESS_ID);
    performDelete(addressIdUri).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet("wrongpath").andExpect(status().isNotFound()).andReturn();
  }
}
