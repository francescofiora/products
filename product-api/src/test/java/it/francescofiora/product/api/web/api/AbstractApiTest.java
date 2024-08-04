package it.francescofiora.product.api.web.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Support class for Api Test.
 */
public abstract class AbstractApiTest {

  @Value("${spring.security.user.name}")
  private String user;

  @Value("${spring.security.user.password}")
  private String password;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  protected String writeValueAsString(Object value) throws JsonProcessingException {
    return mapper.writeValueAsString(value);
  }

  protected <T> T readValue(MvcResult result, TypeReference<T> valueTypeRef)
      throws JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
    return mapper.readValue(result.getResponse().getContentAsString(), valueTypeRef);
  }

  protected ResultActions performPost(String path, Object content) throws Exception {
    // @formatter:off
    return mvc.perform(post(new URI(path))
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPost(String path, Long id, Object content) throws Exception {
    // @formatter:off
    return mvc.perform(post(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPostForbidden(String path, Object content)
      throws Exception {
    // @formatter:off
    return mvc.perform(post(new URI(path))
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeadersWithWrongUser())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPostForbidden(String path, Long id, Object content)
      throws Exception {
    // @formatter:off
    return mvc.perform(post(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeadersWithWrongUser())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPut(String path, Long id, Object content) throws Exception {
    // @formatter:off
    return mvc.perform(put(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPutForbidden(String path, Long id, Object content)
      throws Exception {
    // @formatter:off
    return mvc.perform(put(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeadersWithWrongUser())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPatch(String path, Long id, Object content) throws Exception {
    // @formatter:off
    return mvc.perform(patch(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPatchForbidden(String path, Long id, Object content)
      throws Exception {
    // @formatter:off
    return mvc.perform(patch(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeadersWithWrongUser())
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performGet(String path, Long id) throws Exception {
    return mvc.perform(get(path, id).headers(createHttpHeaders()));
  }

  protected ResultActions performGet(String path, Pageable pageable) throws Exception {
    // @formatter:off
    return mvc.perform(get(new URI(path))
        .headers(createHttpHeaders())
        .contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable)));
    // @formatter:on
  }

  protected ResultActions performGet(String path) throws Exception {
    return mvc.perform(get(new URI(path)).headers(createHttpHeaders()));
  }

  protected ResultActions performGetWithNoUser(String path) throws Exception {
    return mvc.perform(get(new URI(path)).headers(createHttpHeadersWithNoUser()));
  }

  protected ResultActions performGetForbidden(String path, Long id) throws Exception {
    return mvc.perform(get(path, id).headers(createHttpHeadersWithWrongUser()));
  }

  protected ResultActions performGetForbidden(String path, Pageable pageable) throws Exception {
    // @formatter:off
    return mvc.perform(get(new URI(path))
        .headers(createHttpHeadersWithWrongUser())
        .contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable)));
    // @formatter:on
  }

  protected ResultActions performDelete(String path, Object... uriVars) throws Exception {
    return mvc.perform(delete(path, uriVars).headers(createHttpHeaders()));
  }

  protected ResultActions performDeleteForbidden(String path, Object... uriVars) throws Exception {
    return mvc.perform(delete(path, uriVars).headers(createHttpHeadersWithWrongUser()));
  }

  private HttpHeaders createHttpHeadersWithWrongUser() {
    return createHttpHeaders("WRONG");
  }

  private HttpHeaders createHttpHeadersWithNoUser() {
    return createHttpHeaders(null);
  }

  private HttpHeaders createHttpHeaders() {
    return createHttpHeaders(user);
  }

  private HttpHeaders createHttpHeaders(String httpUser) {
    var headers = new HttpHeaders();
    if (httpUser != null) {
      headers.setBasicAuth(httpUser, password);
    }
    return headers;
  }
}
