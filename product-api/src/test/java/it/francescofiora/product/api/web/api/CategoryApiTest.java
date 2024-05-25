package it.francescofiora.product.api.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.api.config.SecurityConfig;
import it.francescofiora.product.api.service.CategoryService;
import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.service.dto.NewCategoryDto;
import it.francescofiora.product.api.util.TestUtils;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CategoryApi.class)
@Import({BuildProperties.class, SecurityConfig.class})
class CategoryApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String CATEGORIES_URI = "/api/v1/categories";
  private static final String CATEGORIES_ID_URI = "/api/v1/categories/{id}";
  private static final String WRONG_URI = "/api/v1/wrong";

  @MockBean
  private CategoryService categoryService;

  @Test
  void testCreate() throws Exception {
    var newCategoryDto = TestUtils.createNewCategoryDto();

    var categoryDto = new CategoryDto();
    categoryDto.setId(ID);
    given(categoryService.create(any(NewCategoryDto.class))).willReturn(categoryDto);

    var result = performPost(CATEGORIES_URI, newCategoryDto).andExpect(status().isCreated())
        .andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(CATEGORIES_URI + "/" + ID);
  }

  @Test
  void testCreateForbidden() throws Exception {
    var newCategoryDto = TestUtils.createNewCategoryDto();

    performPostForbidden(CATEGORIES_URI, newCategoryDto).andExpect(status().isUnauthorized());
  }

  @Test
  void testCreateBadRequest() throws Exception {
    // Name
    var categoryDto = TestUtils.createNewCategoryDto();
    categoryDto.setName(null);
    performPost(CATEGORIES_URI, categoryDto).andExpect(status().isBadRequest());

    categoryDto = TestUtils.createNewCategoryDto();
    categoryDto.setName("");
    performPost(CATEGORIES_URI, categoryDto).andExpect(status().isBadRequest());

    categoryDto = TestUtils.createNewCategoryDto();
    categoryDto.setName("  ");
    performPost(CATEGORIES_URI, categoryDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateBadRequest() throws Exception {
    // id
    var categoryDto = TestUtils.createCategoryDto(null);
    performPut(CATEGORIES_ID_URI, ID, categoryDto).andExpect(status().isBadRequest());

    // Name
    categoryDto = TestUtils.createCategoryDto(ID);
    categoryDto.setName(null);
    performPut(CATEGORIES_ID_URI, ID, categoryDto).andExpect(status().isBadRequest());

    categoryDto = TestUtils.createCategoryDto(ID);
    categoryDto.setName("");
    performPut(CATEGORIES_ID_URI, ID, categoryDto).andExpect(status().isBadRequest());

    categoryDto = TestUtils.createCategoryDto(ID);
    categoryDto.setName("  ");
    performPut(CATEGORIES_ID_URI, ID, categoryDto).andExpect(status().isBadRequest());
  }

  @Test
  void testUpdate() throws Exception {
    var categoryDto = TestUtils.createCategoryDto(ID);
    performPut(CATEGORIES_ID_URI, ID, categoryDto).andExpect(status().isOk());
  }

  @Test
  void testUpdateForbidden() throws Exception {
    var categoryDto = TestUtils.createCategoryDto(ID);

    performPutForbidden(CATEGORIES_ID_URI, ID, categoryDto).andExpect(status().isUnauthorized());
  }

  @Test
  void testGetAll() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = new CategoryDto();
    expected.setId(ID);
    given(categoryService.findAll(any(), any(), any(Pageable.class)))
        .willReturn(new PageImpl<CategoryDto>(List.of(expected)));

    var result = performGet(CATEGORIES_URI, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<CategoryDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetAllForbidden() throws Exception {
    var pageable = PageRequest.of(1, 1);
    performGetForbidden(CATEGORIES_URI, pageable).andExpect(status().isUnauthorized());
  }

  @Test
  void testGet() throws Exception {
    var expected = new CategoryDto();
    expected.setId(ID);
    given(categoryService.findOne(ID)).willReturn(Optional.of(expected));
    var result = performGet(CATEGORIES_ID_URI, ID).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<CategoryDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testGetForbidden() throws Exception {
    performGetForbidden(CATEGORIES_ID_URI, ID).andExpect(status().isUnauthorized());
  }

  @Test
  void testDelete() throws Exception {
    performDelete(CATEGORIES_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testDeleteForbidden() throws Exception {
    performDeleteForbidden(CATEGORIES_ID_URI, ID).andExpect(status().isUnauthorized()).andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
