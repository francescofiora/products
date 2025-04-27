package it.francescofiora.product.api.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.api.config.SecurityConfig;
import it.francescofiora.product.api.service.ProductService;
import it.francescofiora.product.api.service.dto.NewProductDto;
import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.util.TestUtils;
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
@WebMvcTest(controllers = ProductApi.class)
@Import({BuildProperties.class, SecurityConfig.class})
class ProductApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String PRODUCTS_URI = "/api/v1/products";
  private static final String PRODUCTS_ID_URI = "/api/v1/products/{id}";
  private static final String WRONG_URI = "/api/v1/wrong";

  @MockitoBean
  private ProductService productService;

  @Test
  void testCreate() throws Exception {
    var newProductDto = TestUtils.createNewProductDto();

    var productDto = new ProductDto();
    productDto.setId(ID);
    given(productService.create(any(NewProductDto.class))).willReturn(productDto);

    var result =
        performPost(PRODUCTS_URI, newProductDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(PRODUCTS_URI + "/" + ID);
  }

  @Test
  void testCreateForbidden() throws Exception {
    var newProductDto = TestUtils.createNewProductDto();

    performPostForbidden(PRODUCTS_URI, newProductDto).andExpect(status().isUnauthorized());
  }

  @Test
  void testCreateBadRequest() throws Exception {
    // Name
    var productDto = TestUtils.createNewProductDto();
    productDto.setName(null);
    performPost(PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createNewProductDto();
    productDto.setName("");
    performPost(PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createNewProductDto();
    productDto.setName("  ");
    performPost(PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    // Category
    productDto = TestUtils.createNewProductDto();
    productDto.setCategory(null);
    performPost(PRODUCTS_URI, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createNewProductDto();
    productDto.getCategory().setId(null);
    performPost(PRODUCTS_URI, productDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    // id
    var productDto = TestUtils.createUpdatebleProductDto(null);
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    // Name
    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setName(null);
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setName("");
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setName("  ");
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    // Category
    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.setCategory(null);
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());

    productDto = TestUtils.createUpdatebleProductDto(ID);
    productDto.getCategory().setId(null);
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateProduct() throws Exception {
    var productDto = TestUtils.createUpdatebleProductDto(ID);
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isOk());
  }

  @Test
  void testUpdateForbidden() throws Exception {
    var productDto = TestUtils.createUpdatebleProductDto(ID);

    performPutForbidden(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isUnauthorized());
  }

  @Test
  void testGetAllProducts() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = new ProductDto();
    expected.setId(ID);
    given(productService.findAll(any(), any(), any(), any(Pageable.class)))
        .willReturn(new PageImpl<ProductDto>(List.of(expected)));

    var result = performGet(PRODUCTS_URI, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<ProductDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetAllForbidden() throws Exception {
    var pageable = PageRequest.of(1, 1);

    performGetForbidden(PRODUCTS_URI, pageable).andExpect(status().isUnauthorized());
  }

  @Test
  void testGetProduct() throws Exception {
    var expected = new ProductDto();
    expected.setId(ID);
    given(productService.findOne(ID)).willReturn(Optional.of(expected));
    var result = performGet(PRODUCTS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<ProductDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testGetForbidden() throws Exception {
    performGetForbidden(PRODUCTS_ID_URI, ID).andExpect(status().isUnauthorized());
  }

  @Test
  void testDeleteProduct() throws Exception {
    performDelete(PRODUCTS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testDeleteForbidden() throws Exception {
    performDeleteForbidden(PRODUCTS_ID_URI, ID).andExpect(status().isUnauthorized());
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
