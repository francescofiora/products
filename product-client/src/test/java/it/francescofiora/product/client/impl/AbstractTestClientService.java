package it.francescofiora.product.client.impl;

import it.francescofiora.product.client.ClientInfo;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Abstract Test ClientService.
 */
public abstract class AbstractTestClientService {

  private static MockWebServer mockWebServer;

  protected static MockResponse createMockResponse(String body) {
    return new MockResponse().addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .setBody(body).throttleBody(16, 5, TimeUnit.SECONDS).setResponseCode(HttpStatus.OK.value());
  }

  protected static MockResponse createMockBadRequestResponse() {
    return new MockResponse().setResponseCode(HttpStatus.BAD_REQUEST.value());
  }
  
  protected ClientInfo createClientInfo() {
    return new ClientInfo() {

      @Override
      public String getBaseUrl() {
        return mockWebServer.url("/").toString();
      }

      @Override
      public String getUserName() {
        return "user";
      }

      @Override
      public String getPassword() {
        return "password";
      }

      @Override
      public String getKeyStoreFile() {
        return null;
      }

      @Override
      public String getKeyStorePassword() {
        return null;
      }

      @Override
      public boolean isSslEnabled() {
        return false;
      }
    };
  }

  protected static void startServer(Dispatcher dispatcher) throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    mockWebServer.setDispatcher(dispatcher);
  }

  protected static void stopServer() throws IOException {
    mockWebServer.shutdown();
  }
}
