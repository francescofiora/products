package it.francescofiora.product.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductApi.class)
public class ProductApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String PRODUCTS_URI = "/api/products";
  private static final String PRODUCTS_ID_URI = "/api/products/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @MockBean
  private ProductService productService;

  @Test
  public void testCreateProduct() throws Exception {
    NewProductDto newProductDto = TestUtils.createNewProductDto();

    ProductDto productDto = new ProductDto();
    productDto.setId(ID);
    given(productService.create(any(NewProductDto.class))).willReturn(productDto);

    MvcResult result =
        performPost(PRODUCTS_URI, newProductDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(PRODUCTS_URI + "/" + ID);
  }

  @Test
  public void testCreateProductBadRequest() throws Exception {
    // Name
    NewProductDto productDto = TestUtils.createNewProductDto();
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
  public void testUpdateProductBadRequest() throws Exception {
    // id
    UpdatebleProductDto productDto = TestUtils.createUpdatebleProductDto(null);
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
  public void testUpdateProduct() throws Exception {
    UpdatebleProductDto productDto = TestUtils.createUpdatebleProductDto(ID);
    performPut(PRODUCTS_ID_URI, ID, productDto).andExpect(status().isOk());
  }

  @Test
  public void testGetAllProducts() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    ProductDto expected = new ProductDto();
    expected.setId(ID);
    given(productService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<ProductDto>(Collections.singletonList(expected)));

    MvcResult result = performGet(PRODUCTS_URI, pageable).andExpect(status().isOk()).andReturn();
    List<ProductDto> list = readValue(result, new TypeReference<List<ProductDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetProduct() throws Exception {
    ProductDto expected = new ProductDto();
    expected.setId(ID);
    given(productService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(PRODUCTS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    ProductDto actual = readValue(result, new TypeReference<ProductDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeleteProduct() throws Exception {
    performDelete(PRODUCTS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
