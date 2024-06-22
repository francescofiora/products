package it.francescofiora.product.company.client.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Feign Client Configuration.
 */
public class FeignCompanyConfiguration {
  /**
   * Create a new BasicAuthRequestInterceptor.
   *
   * @param username the username from properties file
   * @param password the password from properties file
   * @return a new BasicAuthRequestInterceptor
   */
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
      @Value("${company.userName}") String username,
      @Value("${company.password}") String password) {
    return new BasicAuthRequestInterceptor(username, password);
  }

}
