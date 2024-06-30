package it.francescofiora.product.company.client;

import feign.Headers;
import it.francescofiora.product.company.api.web.api.CompanyApi;
import it.francescofiora.product.company.client.config.FeignCompanyConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Actuator Client Service.
 */
@FeignClient(name = "COMPANY-API", contextId = "COMPANY-SERVICE",
    configuration = FeignCompanyConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface CompanyApiService extends CompanyApi {

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
