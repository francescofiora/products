package it.francescofiora.product.company.client;

import feign.Headers;
import it.francescofiora.product.company.api.web.api.CompanyApi;
import it.francescofiora.product.company.client.config.FeignCompanyConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Actuator Client Service.
 */
@FeignClient(name = "COMPANY-API", contextId = "COMPANY-SERVICE", path = "${company.path:}",
    configuration = FeignCompanyConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface CompanyApiService extends CompanyApi {
}
