package it.francescofiora.product.api.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.CategoryDto;
import it.francescofiora.product.api.util.TestUtils;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class CategoryEndToEndTest extends AbstractTestEndToEnd {

  private static final String CATEGORIES_URI = "/api/v1/categories";
  private static final String CATEGORIES_ID_URI = "/api/v1/categories/%d";

  private static final String ALERT_CREATED = "CategoryDto.created";
  private static final String ALERT_UPDATED = "CategoryDto.updated";
  private static final String ALERT_DELETED = "CategoryDto.deleted";
  private static final String ALERT_GET = "CategoryDto.get";
  private static final String ALERT_BAD_REQUEST = "CategoryDto.badRequest";
  private static final String ALERT_NOT_FOUND = "CategoryDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_ID_NOT_NULL = "[categoryDto.id - NotNull]";
  private static final String PARAM_NAME_NOT_BLANK = "[categoryDto.name - NotBlank]";
  private static final String PARAM_DESCRIPTION_NOT_BLANK = "[categoryDto.description - NotBlank]";

  @Test
  void testCreate() {
    var newCategoryDto = TestUtils.createNewCategoryDto();
    var categoryId = createAndReturnId(ADMIN, CATEGORIES_URI, newCategoryDto, ALERT_CREATED);

    final var categoryIdUri = String.format(CATEGORIES_ID_URI, categoryId);

    var categoryDto = TestUtils.createCategoryDto(categoryId);
    update(ADMIN, categoryIdUri, categoryDto, ALERT_UPDATED, String.valueOf(categoryId));

    var actual =
        get(ADMIN, categoryIdUri, CategoryDto.class, ALERT_GET, String.valueOf(categoryId));
    assertThat(actual).isEqualTo(categoryDto);
    assertThat(actual.getName()).isEqualTo(categoryDto.getName());
    assertThat(actual.getDescription()).isEqualTo(categoryDto.getDescription());

    var categories = get(ADMIN, CATEGORIES_URI, PageRequest.of(1, 1), CategoryDto[].class,
        ALERT_GET, PARAM_PAGE_20);
    assertThat(categories).isNotEmpty();
    var option =
        Stream.of(categories).filter(category -> category.getId().equals(categoryId)).findAny();
    assertThat(option).isPresent().contains(categoryDto);

    delete(ADMIN, categoryIdUri, ALERT_DELETED, String.valueOf(categoryId));

    assertGetNotFound(ADMIN, categoryIdUri, CategoryDto.class, ALERT_NOT_FOUND,
        String.valueOf(categoryId));
  }

  @Test
  void testUnauthorized() {
    testPostUnauthorized(CATEGORIES_URI, TestUtils.createNewCategoryDto());

    testPutUnauthorized(String.format(CATEGORIES_ID_URI, 1L), TestUtils.createCategoryDto(1L));

    testGetUnauthorized(CATEGORIES_URI);

    testGetUnauthorized(String.format(CATEGORIES_ID_URI, 1L));

    testDeleteUnauthorized(String.format(CATEGORIES_ID_URI, 1L));
  }

  @Test
  void testGetBadRequest() {
    assertGetBadRequest(ADMIN, CATEGORIES_URI + "/999999999999999999999999", String.class,
        "id.badRequest", PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() {
    // id
    assertUpdateBadRequest(ADMIN, String.format(CATEGORIES_ID_URI, 1L),
        TestUtils.createCategoryDto(null), ALERT_BAD_REQUEST, PARAM_ID_NOT_NULL);

    var id =
        createAndReturnId(ADMIN, CATEGORIES_URI, TestUtils.createNewCategoryDto(), ALERT_CREATED);

    assertUpdateBadRequest(ADMIN, String.format(CATEGORIES_ID_URI, (id + 1)),
        TestUtils.createCategoryDto(id), ALERT_BAD_REQUEST, String.valueOf(id));

    final String path = String.format(CATEGORIES_ID_URI, id);

    // Name
    var categoryDto = TestUtils.createCategoryDto(id);
    categoryDto.setName(null);
    assertUpdateBadRequest(ADMIN, path, categoryDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    categoryDto = TestUtils.createCategoryDto(id);
    categoryDto.setName("");
    assertUpdateBadRequest(ADMIN, path, categoryDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    categoryDto = TestUtils.createCategoryDto(id);
    categoryDto.setName("  ");
    assertUpdateBadRequest(ADMIN, path, categoryDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    // description
    categoryDto = TestUtils.createCategoryDto(id);
    categoryDto.setDescription(null);
    assertUpdateBadRequest(ADMIN, path, categoryDto, ALERT_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    categoryDto = TestUtils.createCategoryDto(id);
    categoryDto.setDescription("");
    assertUpdateBadRequest(ADMIN, path, categoryDto, ALERT_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    categoryDto = TestUtils.createCategoryDto(id);
    categoryDto.setDescription("  ");
    assertUpdateBadRequest(ADMIN, path, categoryDto, ALERT_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);
  }
}
