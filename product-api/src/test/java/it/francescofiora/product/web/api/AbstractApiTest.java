package it.francescofiora.product.web.api;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
import it.francescofiora.product.util.TestUtils;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

public abstract class AbstractApiTest {

  protected static final String ADMIN = "admin";
  protected static final String USER = "user";
  protected static final String USER_NOT_EXIST = "wrong_user";
  protected static final String USER_WITH_WRONG_ROLE = "wrong_role";
  private static final String PASSWORD = "password";
  private static final String ROLE_USER = "USER";
  private static final String ROLE_ADMIN = "ADMIN";
  private static final String ROLE_NOT_EXIST = "NOT_EXIST";

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private UserDetailsService userDetailsService;

  @BeforeEach
  void setUp() {
    given(userDetailsService.loadUserByUsername(eq(USER)))
        .willReturn(TestUtils.createUser(USER, PASSWORD, ROLE_USER));

    given(userDetailsService.loadUserByUsername(eq(ADMIN)))
        .willReturn(TestUtils.createUser(ADMIN, PASSWORD, ROLE_ADMIN));

    given(userDetailsService.loadUserByUsername(eq(USER_WITH_WRONG_ROLE)))
        .willReturn(TestUtils.createUser(USER_WITH_WRONG_ROLE, PASSWORD, ROLE_NOT_EXIST));
  }

  protected String writeValueAsString(Object value) throws JsonProcessingException {
    return mapper.writeValueAsString(value);
  }

  protected <T> T readValue(MvcResult result, TypeReference<T> valueTypeRef)
      throws JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
    return mapper.readValue(result.getResponse().getContentAsString(), valueTypeRef);
  }

  protected ResultActions performPost(String username, String path, Object content)
      throws Exception {
    // @formatter:off
    return mvc.perform(post(new URI(path))
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders(username))
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPut(String username, String path, Long id, Object content)
      throws Exception {
    // @formatter:off
    return mvc.perform(put(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders(username))
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performPatch(String username, String path, Long id, Object content)
      throws Exception {
    // @formatter:off
    return mvc.perform(patch(path, id)
        .contentType(APPLICATION_JSON)
        .headers(createHttpHeaders(username))
        .content(writeValueAsString(content)));
    // @formatter:on
  }

  protected ResultActions performGet(String username, String path, Long id) throws Exception {
    return mvc.perform(get(path, id).headers(createHttpHeaders(username)));
  }

  protected ResultActions performGet(String username, String path, Pageable pageable)
      throws Exception {
    // @formatter:off
    return mvc.perform(get(new URI(path))
        .headers(createHttpHeaders(username))
        .contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable)));
    // @formatter:on
  }

  protected ResultActions performGet(String username, String path, Long id, Pageable pageable)
      throws Exception {
    // @formatter:off
    return mvc.perform(get(path, id)
        .headers(createHttpHeaders(username))
        .contentType(APPLICATION_JSON)
        .content(writeValueAsString(pageable)));
    // @formatter:on
  }

  protected ResultActions performGet(String username, String path) throws Exception {
    return mvc.perform(get(new URI(path)).headers(createHttpHeaders(username)));
  }

  protected ResultActions performDelete(String username, String path, Object... uriVars)
      throws Exception {
    return mvc.perform(delete(path, uriVars).headers(createHttpHeaders(username)));
  }

  private HttpHeaders createHttpHeaders(String username) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(username, PASSWORD);
    return headers;
  }
}
