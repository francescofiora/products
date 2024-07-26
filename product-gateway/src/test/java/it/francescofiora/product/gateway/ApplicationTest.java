package it.francescofiora.product.gateway;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import it.francescofiora.product.gateway.config.UriConfiguration;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
@AutoConfigureWireMock(port = 0)
class ApplicationTest {

  private static final String CATEGORY_URL = "/api/v1/categories";
  private static final String CATEGORY_KEY = "category";

  private static final String ORDER_URL = "/api/v1/orders";
  private static final String ORDER_KEY = "order";

  private static final String PRODUCT_URL = "/api/v1/products";
  private static final String PRODUCT_KEY = "product";

  private static final String COMPANY_URL = "/api/v1/companies";
  private static final String COMPANY_KEY = "company";

  private static final String CONTACT_URL = "/api/v1/contacts";
  private static final String CONTACT_KEY = "contact";

  private static final String JSON_PATH = "$.myText.key";
  private static final String BODY = """
      {
         "myText": {
             "key": "%s"
         }
      }
      """;

  @Autowired
  private WebTestClient webClient;

  @Autowired
  private UriConfiguration uriConfiguration;

  @Value("${spring.security.user.name}")
  private String gatewayUser;

  @Value("${spring.security.user.password}")
  private String gatewayPassword;

  private void configStub(String url, String value, String user, String password) {
    stubFor(get(urlEqualTo(url))
        .withBasicAuth(user, password)
        .willReturn(aResponse()
            .withBody(String.format(BODY, value))
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)));
  }

  private void productConfig() {
    var productUser = uriConfiguration.getProduct().getUserName();
    var productPassword = uriConfiguration.getProduct().getPassword();

    configStub(CATEGORY_URL, CATEGORY_KEY, productUser, productPassword);
    configStub(ORDER_URL, ORDER_KEY, productUser, productPassword);
    configStub(PRODUCT_URL, PRODUCT_KEY, productUser, productPassword);
  }

  private void testUrl(String url, String value) {
    webClient
        .get().uri(url)
        .headers(headers -> headers.setBasicAuth(gatewayUser, gatewayPassword))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath(JSON_PATH).isEqualTo(value);
  }

  @Test
  void testProduct() {
    productConfig();

    testUrl(CATEGORY_URL, CATEGORY_KEY);
    testUrl(ORDER_URL, ORDER_KEY);
    testUrl(PRODUCT_URL, PRODUCT_KEY);
  }

  private void companyConfig() {
    var companyUser = uriConfiguration.getCompany().getUserName();
    var companyPassword = uriConfiguration.getCompany().getPassword();

    configStub(COMPANY_URL, COMPANY_KEY, companyUser, companyPassword);
    configStub(CONTACT_URL, CONTACT_KEY, companyUser, companyPassword);
  }

  @Test
  void testCompany() {
    companyConfig();

    testUrl(COMPANY_URL, COMPANY_KEY);
    testUrl(CONTACT_URL, CONTACT_KEY);
  }
}
