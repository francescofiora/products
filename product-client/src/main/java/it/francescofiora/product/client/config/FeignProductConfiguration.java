package it.francescofiora.product.client.config;

import feign.Feign;
import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import feign.hc5.ApacheHttp5Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Feign Client Configuration.
 */
public class FeignProductConfiguration {

  /**
   * Create a new BasicAuthRequestInterceptor.
   *
   * @param username the username from properties file
   * @param password the password from properties file
   * @return a new BasicAuthRequestInterceptor
   */
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
      @Value("${product.userName}") String username,
      @Value("${product.password}") String password) {
    return new BasicAuthRequestInterceptor(username, password);
  }

  /**
   * Create a new Feign Builder.
   *
   * @return a new Feign Builder
   */
  @Bean
  public Feign.Builder feignBuilder() {
    return Feign.builder()
        .retryer(Retryer.NEVER_RETRY)
        .client(new ApacheHttp5Client());
  }
}
