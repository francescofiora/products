package it.francescofiora.product.company.api.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.api.web.api.impl.AbstractApi;
import it.francescofiora.product.company.api.web.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @LocalServerPort
  private int randomServerPort;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value("${spring.security.user.name}")
  private String user;

  @Value("${spring.security.user.password}")
  private String password;

  private String getPath(String path) {
    return "http://localhost:" + randomServerPort + path;
  }

  protected <T> ResponseEntity<T> performGet(String path, Class<T> responseType) {
    var request = new HttpEntity<>(createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String path, Pageable pageable,
      Class<T> responseType) {
    var request = new HttpEntity<>(pageable, createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected ResponseEntity<Void> performDelete(String path) {
    var request = new HttpEntity<>(createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.DELETE, request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPost(String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders());
    return restTemplate.postForEntity(AbstractApi.createUri(getPath(path)), request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPut(String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.PUT, request, Void.class);
  }

  private void checkHeaders(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> Long createAndReturnId(String path, T body, String alert) {
    var result = performPost(path, body);
    assertThat(result.getHeaders()).containsKeys(HeaderUtil.X_ALERT, HttpHeaders.LOCATION,
        HeaderUtil.X_PARAMS);
    assertThat(result.getHeaders().get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(result.getHeaders().get(HeaderUtil.X_PARAMS)).isNotEmpty();
    var id = getIdFormHttpHeaders(result.getHeaders());
    checkHeaders(result.getHeaders(), alert, String.valueOf(id));
    return id;
  }

  protected <T> void assertCreateNotFound(String path, T body, String alert, String param) {
    var result = performPost(path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertCreateBadRequest(String path, T body, String alert, String param) {
    var result = performPost(path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected <T> void update(String path, T body, String alert, String param) {
    var result = performPut(path, body);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  protected <T> void assertUpdateBadRequest(String path, T body, String alert, String param) {
    var result = performPut(path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected <T> void assertUpdateNotFound(String path, T body, String alert, String param) {
    var result = performPut(path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  private void checkHeadersError(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_ERROR, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_ERROR)).isNotEmpty();
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> T get(String path, Class<T> responseType, String alert, String param) {
    var result = performGet(path, responseType);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkHeaders(result.getHeaders(), alert, param);
    T value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> T get(String path, Pageable pageable, Class<T> responseType, String alert,
      String param) {
    var result = performGet(path, pageable, responseType);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    T value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> void assertGetNotFound(String path, Class<T> responseType, String alert,
      String param) {
    var result = performGet(path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetNotFound(String path, Pageable pageable, Class<T> responseType,
      String alert, String param) {
    var result = performGet(path, pageable, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetBadRequest(String path, Class<T> responseType, String alert,
      String param) {
    var result = performGet(path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected void delete(String path, String alert, String param) {
    var result = performDelete(path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    checkHeaders(result.getHeaders(), alert, param);
  }

  protected void assertDeleteBadRequest(String path, String alert, String param) {
    var result = performDelete(path);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected Long getIdFormHttpHeaders(HttpHeaders headers) {
    var url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  private HttpHeaders createHttpHeaders() {
    var headers = new HttpHeaders();
    headers.setBasicAuth(user, password);
    return headers;
  }
}
