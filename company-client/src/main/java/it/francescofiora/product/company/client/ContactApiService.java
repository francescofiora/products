package it.francescofiora.product.company.client;

import feign.Headers;
import it.francescofiora.product.company.api.web.api.ContactApi;
import it.francescofiora.product.company.client.config.FeignCompanyConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Client Service for ContactApi.
 */
@FeignClient(name = "COMPANY-API", contextId = "CONTACT-SERVICE", path = "${company.path:}",
    configuration = FeignCompanyConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface ContactApiService extends ContactApi {

}
