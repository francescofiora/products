package it.francescofiora.product.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.product.service.CategoryService;
import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;
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
@WebMvcTest(controllers = CategoryApi.class)
class CategoryApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String CATEGORIES_URI = "/api/categories";
  private static final String CATEGORIES_ID_URI = "/api/categories/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @MockBean
  private CategoryService categoryService;

  @Test
  void testCreateCategory() throws Exception {
    NewCategoryDto newCategoryDto = TestUtils.createNewCategoryDto();

    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setId(ID);
    given(categoryService.create(any(NewCategoryDto.class))).willReturn(categoryDto);

    MvcResult result =
        performPost(CATEGORIES_URI, newCategoryDto).andExpect(status().isCreated()).andReturn();

    assertThat(result.getResponse().getHeaderValue(HttpHeaders.LOCATION))
        .isEqualTo(CATEGORIES_URI + "/" + ID);
  }

  @Test
  void testCreateCategoryBadRequest() throws Exception {
    // Name
    NewCategoryDto categoryDto = TestUtils.createNewCategoryDto();
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
  void testUpdateCategoryBadRequest() throws Exception {
    // id
    CategoryDto categoryDto = TestUtils.createCategoryDto(null);
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
  void testUpdateCategory() throws Exception {
    CategoryDto categoryDto = TestUtils.createCategoryDto(ID);
    performPut(CATEGORIES_ID_URI, ID, categoryDto).andExpect(status().isOk());
  }

  @Test
  void testGetAllCategories() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    CategoryDto expected = new CategoryDto();
    expected.setId(ID);
    given(categoryService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<CategoryDto>(Collections.singletonList(expected)));

    MvcResult result = performGet(CATEGORIES_URI, pageable).andExpect(status().isOk()).andReturn();
    List<CategoryDto> list = readValue(result, new TypeReference<List<CategoryDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetCategory() throws Exception {
    CategoryDto expected = new CategoryDto();
    expected.setId(ID);
    given(categoryService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(CATEGORIES_ID_URI, ID).andExpect(status().isOk()).andReturn();
    CategoryDto actual = readValue(result, new TypeReference<CategoryDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testDeleteCategory() throws Exception {
    performDelete(CATEGORIES_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
