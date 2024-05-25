package it.francescofiora.product.api.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.dto.ProductDto;
import it.francescofiora.product.api.util.TestUtils;
import java.math.BigDecimal;
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
class ProductEndToEndTest extends AbstractTestEndToEnd {

  private static final String CATEGORIES_URI = "/api/v1/categories";
  private static final String PRODUCTS_URI = "/api/v1/products";
  private static final String PRODUCTS_ID_URI = "/api/v1/products/%d";

  private static final String ALERT_CATEGORY_CREATED = "CategoryDto.created";

  private static final String ALERT_CREATED = "ProductDto.created";
  private static final String ALERT_UPDATED = "ProductDto.updated";
  private static final String ALERT_DELETED = "ProductDto.deleted";
  private static final String ALERT_GET = "ProductDto.get";
  private static final String ALERT_UPDATE_BAD_REQUEST = "UpdatebleProductDto.badRequest";
  private static final String ALERT_NOT_FOUND = "ProductDto.notFound";
  private static final String ALERT_CATEGORY_NOT_FOUND = "CategoryDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_ID_NOT_NULL = "[updatebleProductDto.id - NotNull]";
  private static final String PARAM_NAME_NOT_BLANK = "[updatebleProductDto.name - NotBlank]";
  private static final String PARAM_DESCRIPTION_NOT_BLANK =
      "[updatebleProductDto.description - NotBlank]";
  private static final String PARAM_PRICE_NOT_NULL = "[updatebleProductDto.price - NotNull]";
  private static final String PARAM_PRICE_MIN = "[updatebleProductDto.price - DecimalMin]";
  private static final String PARAM_SIZE_NOT_NULL = "[updatebleProductDto.size - NotNull]";

  @Test
  void testCreate() {
    var categoryId = createAndReturnId(CATEGORIES_URI, TestUtils.createNewCategoryDto(),
        ALERT_CATEGORY_CREATED);

    var newProductDto = TestUtils.createNewProductDto();
    newProductDto.setCategory(TestUtils.createRefCategoryDto(categoryId));
    var productId = createAndReturnId(PRODUCTS_URI, newProductDto, ALERT_CREATED);

    final var productIdUri = String.format(PRODUCTS_ID_URI, productId);

    categoryId = createAndReturnId(CATEGORIES_URI, TestUtils.createNewCategoryDto(),
        ALERT_CATEGORY_CREATED);
    var productDto = TestUtils.createUpdatebleProductDto(productId);
    productDto.setCategory(TestUtils.createRefCategoryDto(categoryId));
    update(productIdUri, productDto, ALERT_UPDATED, String.valueOf(productId));

    var actual =
        get(productIdUri, ProductDto.class, ALERT_GET, String.valueOf(productId));
    assertThat(actual.getId()).isEqualTo(productDto.getId());
    assertThat(actual.getCategory().getId()).isEqualTo(productDto.getCategory().getId());
    assertThat(actual.getName()).isEqualTo(productDto.getName());
    assertThat(actual.getDescription()).isEqualTo(productDto.getDescription());
    assertThat(actual.getImage()).isEqualTo(productDto.getImage());
    assertThat(actual.getImageContentType()).isEqualTo(productDto.getImageContentType());
    assertThat(actual.getPrice().toBigInteger()).isEqualTo(productDto.getPrice().toBigInteger());
    assertThat(actual.getSize()).isEqualTo(productDto.getSize());

    var products = get(PRODUCTS_URI, PageRequest.of(1, 1), ProductDto[].class,
        ALERT_GET, PARAM_PAGE_20);
    assertThat(products).isNotEmpty();
    var option =
        Stream.of(products).filter(product -> product.getId().equals(productId)).findAny();
    assertThat(option).isPresent().contains(actual);

    delete(productIdUri, ALERT_DELETED, String.valueOf(productId));

    assertGetNotFound(productIdUri, ProductDto.class, ALERT_NOT_FOUND,
        String.valueOf(productId));
  }

  @Test
  void testCreateBadRequest() {
    var newProductDto = TestUtils.createNewProductDto();
    newProductDto.setCategory(TestUtils.createRefCategoryDto(100L));
    assertCreateNotFound(PRODUCTS_URI, newProductDto, ALERT_CATEGORY_NOT_FOUND,
        String.valueOf(newProductDto.getCategory().getId()));
  }

  @Test
  void testUnauthorized() {
    testPostUnauthorized(PRODUCTS_URI, TestUtils.createNewProductDto());

    testPutUnauthorized(String.format(PRODUCTS_ID_URI, 1L),
        TestUtils.createUpdatebleProductDto(1L));

    testGetUnauthorized(PRODUCTS_URI);

    testGetUnauthorized(String.format(PRODUCTS_ID_URI, 1L));

    testDeleteUnauthorized(String.format(PRODUCTS_ID_URI, 1L));
  }

  @Test
  void testGetBadRequest() {
    assertGetBadRequest(PRODUCTS_URI + "/999999999999999999999999", String.class,
        "id.badRequest", PARAM_NOT_VALID_LONG);
  }

  @Test
  void testUpdateBadRequest() {
    // id
    assertUpdateBadRequest(String.format(PRODUCTS_ID_URI, 1L),
        TestUtils.createUpdatebleProductDto(null), ALERT_UPDATE_BAD_REQUEST, PARAM_ID_NOT_NULL);

    var categoryId = createAndReturnId(CATEGORIES_URI, TestUtils.createNewCategoryDto(),
        ALERT_CATEGORY_CREATED);
    var newProductDto = TestUtils.createNewProductDto();
    newProductDto.setCategory(TestUtils.createRefCategoryDto(categoryId));
    var id = createAndReturnId(PRODUCTS_URI, newProductDto, ALERT_CREATED);

    assertUpdateBadRequest(String.format(PRODUCTS_ID_URI, (id + 1)),
        TestUtils.createUpdatebleProductDto(id), ALERT_UPDATE_BAD_REQUEST, String.valueOf(id));

    final var path = String.format(PRODUCTS_ID_URI, id);

    // Name
    var productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setName(null);
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setName("");
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setName("  ");
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST, PARAM_NAME_NOT_BLANK);

    // description
    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setDescription(null);
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setDescription("");
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setDescription("  ");
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST,
        PARAM_DESCRIPTION_NOT_BLANK);

    // price
    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setPrice(null);
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST, PARAM_PRICE_NOT_NULL);

    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setPrice(BigDecimal.valueOf(-1L));
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST, PARAM_PRICE_MIN);

    // size
    productDto = TestUtils.createUpdatebleProductDto(id);
    productDto.setSize(null);
    assertUpdateBadRequest(path, productDto, ALERT_UPDATE_BAD_REQUEST, PARAM_SIZE_NOT_NULL);
  }
}
