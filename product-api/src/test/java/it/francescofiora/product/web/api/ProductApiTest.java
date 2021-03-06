package it.francescofiora.product.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.config.MethodSecurityConfig;
import it.francescofiora.product.service.ProductService;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.ProductDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;
import it.francescofiora.product.util.TestUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductApi.class)
@Import(MethodSecurityConfig.class)
class ProductApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String PRODUCTS_URI = "/api/products";
  private static final String PRODUCTS_ID_URI = "/api/products/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @MockBean
  private ProductService productService;

  @Test
  void testCreate() throws Exception {
    NewProductDto newProductDto = TestUtils.createNewProductDto();

    ProductDto productDto = new ProductDto();
    productDto.setId(ID);
    given(productService.create(any(NewProductDto.class))).willReturn(productDto);

    MvcResult result =
        performPost(ADMIN, PRODUCTS_URI, newProductDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(PRODUCTS_URI + "/" + ID);
  }

  @Test
  void testCreateForbidden() throws Exception {
    NewProductDto newProductDto = TestUtils.createNewProductDto();

    performPost(USER, PRODUCTS_URI, newProductDto).andExpect(status().isForbidden());

    performPost(USER_NOT_EXIST, PRODUCTS_URI, newProductDto).andExpect(status().isUnauthorized());

    performPost(USER_WITH_WRONG_ROLE, PRODUCTS_URI, newProductDto)
        .andExpect(status().isForbidden());
  }

  @Test
  void testCreateBadRequest() throws Exception {
    // Name
    NewProductDto productDto = TestUtils.createNewProductDto();
    productDto.setName(null);
    performPost(ADMIN, PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createNewProductDto();
    productDto.setName("");
    performPost(ADMIN, PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createNewProductDto();
    productDto.setName("  ");
    performPost(ADMIN, PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    // Category
    productDto = TestUtils.createNewProductDto();
    productDto.setCategory(null);
    performPost(ADMIN, PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createNewProductDto();
    productDto.getCategory().setId(null);
    performPost(ADMIN, PRODUCTS_URI, productDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    // id
    UpdatebleProductDto productDto = TestUtils.createUpdatebleProductDto(null);
    performPut(ADMIN, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    // Name
    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setName(null);
    performPut(ADMIN, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setName("");
    performPut(ADMIN, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setName("  ");
    performPut(ADMIN, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    // Category
    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setCategory(null);
    performPut(ADMIN, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.getCategory().setId(null);
    performPut(ADMIN, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateProduct() throws Exception {
    UpdatebleProductDto productDto = TestUtils.createUpdatebleProductDto(ID);
    performPut(ADMIN, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isOk());
  }

  @Test
  void testUpdateForbidden() throws Exception {
    UpdatebleProductDto productDto = TestUtils.createUpdatebleProductDto(ID);

    performPut(USER, PRODUCTS_ID_URI, ID, productDto).andExpect(status().isForbidden());

    performPut(USER_NOT_EXIST, PRODUCTS_ID_URI, ID, productDto)
        .andExpect(status().isUnauthorized());

    performPut(USER_WITH_WRONG_ROLE, PRODUCTS_ID_URI, ID, productDto)
        .andExpect(status().isForbidden());
  }

  @Test
  void testGetAllProducts() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    ProductDto expected = new ProductDto();
    expected.setId(ID);
    given(productService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<ProductDto>(Collections.singletonList(expected)));

    MvcResult result =
        performGet(USER, PRODUCTS_URI, pageable).andExpect(status().isOk()).andReturn();
    List<ProductDto> list = readValue(result, new TypeReference<List<ProductDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetAllForbidden() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);

    performGet(USER_NOT_EXIST, PRODUCTS_URI, pageable).andExpect(status().isUnauthorized());

    performGet(USER_WITH_WRONG_ROLE, PRODUCTS_URI, pageable).andExpect(status().isForbidden());
  }

  @Test
  void testGetProduct() throws Exception {
    ProductDto expected = new ProductDto();
    expected.setId(ID);
    given(productService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(USER, PRODUCTS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    ProductDto actual = readValue(result, new TypeReference<ProductDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testGetForbidden() throws Exception {
    performGet(USER_NOT_EXIST, PRODUCTS_ID_URI, ID).andExpect(status().isUnauthorized());

    performGet(USER_WITH_WRONG_ROLE, PRODUCTS_ID_URI, ID).andExpect(status().isForbidden());
  }

  @Test
  void testDeleteProduct() throws Exception {
    performDelete(ADMIN, PRODUCTS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testDeleteForbidden() throws Exception {
    performDelete(USER, PRODUCTS_ID_URI, ID).andExpect(status().isForbidden());

    performDelete(USER_NOT_EXIST, PRODUCTS_ID_URI, ID).andExpect(status().isUnauthorized());

    performDelete(USER_WITH_WRONG_ROLE, PRODUCTS_ID_URI, ID).andExpect(status().isForbidden());
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(USER, WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
