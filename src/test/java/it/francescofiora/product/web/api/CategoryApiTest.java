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
import it.francescofiora.product.service.CategoryService;
import it.francescofiora.product.service.dto.CategoryDto;
import it.francescofiora.product.service.dto.NewCategoryDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CategoryApi.class)
public class CategoryApiTest extends AbstractApiTest {

  private static final Long ID = 1L;
  private static final String CATEGORIES_URI = "/api/categories";
  private static final String CATEGORIES_ID_URI = "/api/categories/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private CategoryService categoryService;

  @Test
  public void testCreateCategory() throws Exception {
    NewCategoryDto newCategoryDto = new NewCategoryDto();
    fillCategory(newCategoryDto);

    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setId(ID);
    given(categoryService.create(any(NewCategoryDto.class))).willReturn(categoryDto);
    MvcResult result = mvc.perform(post(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(newCategoryDto))).andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(CATEGORIES_URI + "/" + ID);
  }

  @Test
  public void testCreateCategoryBadRequest() throws Exception {
    // Name
    NewCategoryDto categoryDto = new NewCategoryDto();
    fillCategory(categoryDto);
    categoryDto.setName(null);
    mvc.perform(post(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isBadRequest());

    fillCategory(categoryDto);
    categoryDto.setName("");
    mvc.perform(post(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isBadRequest());

    fillCategory(categoryDto);
    categoryDto.setName("  ");
    mvc.perform(post(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isBadRequest());
  }
  
  @Test
  public void testUpdateCategoryBadRequest() throws Exception {
    // id
    CategoryDto categoryDto = updateCategoryDto();
    categoryDto.setId(null);
    mvc.perform(put(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isBadRequest());

    // Name
    categoryDto = updateCategoryDto();
    categoryDto.setName(null);
    mvc.perform(put(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isBadRequest());

    categoryDto = updateCategoryDto();
    categoryDto.setName("");
    mvc.perform(put(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isBadRequest());

    categoryDto = updateCategoryDto();
    categoryDto.setName("  ");
    mvc.perform(put(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateCategory() throws Exception {
    CategoryDto categoryDto = updateCategoryDto();
    mvc.perform(put(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(categoryDto))).andExpect(status().isOk());
  }

  private CategoryDto updateCategoryDto() {
    CategoryDto categoryDto = new CategoryDto();
    fillCategory(categoryDto);
    categoryDto.setId(ID);
    return categoryDto;
  }

  private void fillCategory(NewCategoryDto categoryDto) {
    categoryDto.setName("Name");
    categoryDto.setDescription("Description");
  }

  @Test
  public void testGetAllCategories() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    CategoryDto expected = new CategoryDto();
    expected.setId(ID);
    given(categoryService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<CategoryDto>(Collections.singletonList(expected)));

    MvcResult result = mvc.perform(get(new URI(CATEGORIES_URI)).contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<CategoryDto> list = readValue(result, new TypeReference<List<CategoryDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetCategory() throws Exception {
    CategoryDto expected = new CategoryDto();
    expected.setId(ID);
    given(categoryService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(CATEGORIES_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    CategoryDto actual = readValue(result, new TypeReference<CategoryDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeleteCategory() throws Exception {
    mvc.perform(delete(CATEGORIES_ID_URI, ID)).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    mvc.perform(get(WRONG_URI)).andExpect(status().isNotFound()).andReturn();
  }
}
