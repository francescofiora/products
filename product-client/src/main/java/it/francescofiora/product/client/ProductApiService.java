package it.francescofiora.product.client;

import feign.Headers;
import it.francescofiora.product.api.web.api.ProductApi;
import it.francescofiora.product.client.config.FeignProductConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Actuator Client Service.
 */
@FeignClient(name = "PRODUCT-API", contextId = "PRODUCT-SERVICE",
    configuration = FeignProductConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface ProductApiService extends ProductApi {

  /**
   * Get the info.
   *
   * @return the response
   */
  @GetMapping("/actuator/info")
  ResponseEntity<String> getInfo();

  /**
   * Get the health.
   *
   * @return the response
   */
  @GetMapping("/actuator/health")
  ResponseEntity<String> getHealth();
}
