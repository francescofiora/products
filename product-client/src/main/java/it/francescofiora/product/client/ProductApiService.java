package it.francescofiora.product.client;

import feign.Headers;
import it.francescofiora.product.api.web.api.ProductApi;
import it.francescofiora.product.client.config.FeignProductConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Actuator Client Service.
 */
@FeignClient(name = "PRODUCT-API", contextId = "PRODUCT-SERVICE", path = "${product.path:}",
    configuration = FeignProductConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface ProductApiService extends ProductApi {

}
