package it.francescofiora.product.api.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.repository.UserRepository;
import it.francescofiora.product.api.util.TestUtils;
import it.francescofiora.product.api.web.api.AbstractApi;
import it.francescofiora.product.api.web.util.HeaderUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Abstract Test class for EndToEnd tests.
 */
public class AbstractTestEndToEnd {

  protected static final String ADMIN = "admin";
  protected static final String USER = "user";
  protected static final String USER_NOT_EXIST = "wrong_user";
  protected static final String USER_WITH_WRONG_ROLE = "wrong_role";
  private static final String PASSWORD = "password";
  private static final String ROLE_USER = "USER";
  private static final String ROLE_ADMIN = "ADMIN";
  private static final String ROLE_NOT_EXIST = "NOT_EXIST";

  @Autowired
  private UserRepository userRepository;

  @LocalServerPort
  private int randomServerPort;

  @Autowired
  private TestRestTemplate restTemplate;

  private String getPath(String path) {
    return "http://localhost:" + randomServerPort + path;
  }

  @BeforeEach
  @Transactional
  void setUp() {
    var opt = userRepository.findByUsername(USER);
    if (!opt.isPresent()) {
      userRepository.save(TestUtils.createUser(USER, PASSWORD, ROLE_USER));
      userRepository.save(TestUtils.createUser(ADMIN, PASSWORD, ROLE_ADMIN));
      userRepository.save(TestUtils.createUser(USER_WITH_WRONG_ROLE, PASSWORD, ROLE_NOT_EXIST));
    }
  }

  protected void testGetUnauthorized(String path) {
    var result = performGet(null, path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = performGet(USER_NOT_EXIST, path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = performGet(USER_WITH_WRONG_ROLE, path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  protected <T> void testPostUnauthorized(String path, T body) {
    testPostUnauthorized(path, body, false);
  }

  protected <T> void testPostUnauthorized(String path, T body, boolean userAllowed) {
    var result = performPost(null, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    if (!userAllowed) {
      result = performPost(USER, path, body);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    result = performPost(USER_NOT_EXIST, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = performPost(USER_WITH_WRONG_ROLE, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  protected <T> void testPutUnauthorized(String path, T body) {
    var result = performPut(null, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = performPut(USER, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    result = performPut(USER_NOT_EXIST, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = performPut(USER_WITH_WRONG_ROLE, path, body);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  protected void testDeleteUnauthorized(String path) {
    testDeleteUnauthorized(path, false);
  }

  protected void testDeleteUnauthorized(String path, boolean userAllowed) {
    var result = performDelete(null, path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    if (!userAllowed) {
      result = performDelete(USER, path);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    result = performDelete(USER_NOT_EXIST, path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = performDelete(USER_WITH_WRONG_ROLE, path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  protected <T> ResponseEntity<T> performGet(String username, String path, Class<T> responseType) {
    var request = new HttpEntity<>(createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String username, String path, Pageable pageable,
      Class<T> responseType) {
    var request = new HttpEntity<>(pageable, createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected ResponseEntity<Void> performDelete(String username, String path) {
    var request = new HttpEntity<>(createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.DELETE, request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPost(String username, String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders(username));
    return restTemplate.postForEntity(AbstractApi.createUri(getPath(path)), request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPut(String username, String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.PUT, request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPatch(String username, String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders(username));
    return restTemplate.exchange(getPath(path), HttpMethod.PATCH, request, Void.class);
  }

  private void checkHeaders(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> Long createAndReturnId(String username, String path, T body, String alert) {
    var result = performPost(username, path, body);
    assertThat(result.getHeaders()).containsKeys(HeaderUtil.X_ALERT, HttpHeaders.LOCATION,
        HeaderUtil.X_PARAMS);
    assertThat(result.getHeaders().get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(result.getHeaders().get(HeaderUtil.X_PARAMS)).isNotEmpty();
    var id = getIdFormHttpHeaders(result.getHeaders());
    checkHeaders(result.getHeaders(), alert, String.valueOf(id));
    return id;
  }

  protected <T> void assertCreateNotFound(String username, String path, T body, String alert,
      String param) {
    var result = performPost(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertPatchBadRequest(String username, String path, T body, String alert,
      String param) {
    var result = performPatch(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected <T> void assertCreateBadRequest(String username, String path, T body, String alert,
      String param) {
    var result = performPost(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected <T> void update(String username, String path, T body, String alert, String param) {
    var result = performPut(username, path, body);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  protected <T> void patch(String username, String path, T body, String alert, String param) {
    var result = performPatch(username, path, body);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  protected <T> void assertUpdateBadRequest(String username, String path, T body, String alert,
      String param) {
    var result = performPut(username, path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private void checkHeadersError(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_ERROR, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_ERROR)).isNotEmpty();
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> T get(String username, String path, Class<T> responseType, String alert,
      String param) {
    var result = performGet(username, path, responseType);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkHeaders(result.getHeaders(), alert, param);
    T value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> T get(String username, String path, Pageable pageable, Class<T> responseType,
      String alert, String param) {
    var result = performGet(username, path, pageable, responseType);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    T value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> void assertGetNotFound(String username, String path, Class<T> responseType,
      String alert, String param) {
    var result = performGet(username, path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetNotFound(String username, String path, Pageable pageable,
      Class<T> responseType, String alert, String param) {
    var result = performGet(username, path, pageable, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetBadRequest(String username, String path, Class<T> responseType,
      String alert, String param) {
    var result = performGet(username, path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected void delete(String username, String path, String alert, String param) {
    var result = performDelete(username, path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    checkHeaders(result.getHeaders(), alert, param);
  }

  protected void assertDeleteBadRequest(String username, String path, String alert, String param) {
    var result = performDelete(username, path);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected Long getIdFormHttpHeaders(HttpHeaders headers) {
    var url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  private HttpHeaders createHttpHeaders(String username) {
    var headers = new HttpHeaders();
    if (username != null) {
      headers.setBasicAuth(username, PASSWORD);
    }
    return headers;
  }
}
