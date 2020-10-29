package it.francescofiora.product.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.domain.enumeration.Size;
import it.francescofiora.product.service.ProductService;
import it.francescofiora.product.service.dto.BaseProductDto;
import it.francescofiora.product.service.dto.NewProductDto;
import it.francescofiora.product.service.dto.ProductDto;
import it.francescofiora.product.service.dto.RefCategoryDto;
import it.francescofiora.product.service.dto.UpdatebleProductDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductApi.class)
public class ProductApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String PRODUCTS_URI = "/api/products";
  private static final String PRODUCTS_ID_URI = "/api/products/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ProductService productService;

  @Test
  public void testCreateProduct() throws Exception {
    NewProductDto newProductDto = getNewProductDto();

    ProductDto productDto = new ProductDto();
    productDto.setId(ID);
    given(productService.create(any(NewProductDto.class))).willReturn(productDto);
    MvcResult result = mvc
        .perform(post(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
            .content(writeValueAsString(newProductDto)))
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(PRODUCTS_URI + "/" + ID);
  }

  @Test
  public void testCreateProductBadRequest() throws Exception {
    // Name
    NewProductDto productDto = getNewProductDto();
    productDto.setName(null);
    mvc.perform(post(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    productDto = getNewProductDto();
    productDto.setName("");
    mvc.perform(post(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    productDto = getNewProductDto();
    productDto.setName("  ");
    mvc.perform(post(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    // Category
    productDto = getNewProductDto();
    productDto.setCategory(null);
    mvc.perform(post(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    productDto = getNewProductDto();
    productDto.getCategory().setId(null);
    mvc.perform(post(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());
  }

  private NewProductDto getNewProductDto() {
    NewProductDto newProductDto = new NewProductDto();
    fillProduct(newProductDto);
    newProductDto.setCategory(getRefCategoryDto());
    return newProductDto;
  }

  @Test
  public void testUpdateProductBadRequest() throws Exception {
    // id
    UpdatebleProductDto productDto = updateProductDto();
    productDto.setId(null);
    mvc.perform(put(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    // Name
    productDto = updateProductDto();
    productDto.setName(null);
    mvc.perform(put(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    productDto = updateProductDto();
    productDto.setName("");
    mvc.perform(put(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    productDto = updateProductDto();
    productDto.setName("  ");
    mvc.perform(put(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    // Category
    productDto = updateProductDto();
    productDto.setCategory(null);
    mvc.perform(put(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());

    productDto = updateProductDto();
    productDto.getCategory().setId(null);
    mvc.perform(put(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateProduct() throws Exception {
    UpdatebleProductDto productDto = updateProductDto();
    mvc.perform(put(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(productDto))).andExpect(status().isOk());
  }

  private UpdatebleProductDto updateProductDto() {
    UpdatebleProductDto productDto = new UpdatebleProductDto();
    fillProduct(productDto);
    productDto.setId(ID);
    productDto.setCategory(getRefCategoryDto());
    return productDto;
  }

  private RefCategoryDto getRefCategoryDto() {
    RefCategoryDto categoryDto = new RefCategoryDto();
    categoryDto.setId(ID);
    return categoryDto;
  }

  private void fillProduct(BaseProductDto productDto) {
    productDto.setName("Name");
    productDto.setSize(Size.L);
    productDto.setPrice(BigDecimal.TEN);
  }

  @Test
  public void testGetAllProducts() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    ProductDto expected = new ProductDto();
    expected.setId(ID);
    given(productService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<ProductDto>(Collections.singletonList(expected)));

    MvcResult result = mvc.perform(get(new URI(PRODUCTS_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<ProductDto> list = readValue(result, new TypeReference<List<ProductDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetProduct() throws Exception {
    ProductDto expected = new ProductDto();
    expected.setId(ID);
    given(productService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(PRODUCTS_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    ProductDto actual = readValue(result, new TypeReference<ProductDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeleteProduct() throws Exception {
    mvc.perform(delete(PRODUCTS_ID_URI, ID)).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    mvc.perform(get(WRONG_URI)).andExpect(status().isNotFound()).andReturn();
  }
}
