package it.francescofiora.product.client;

import feign.Headers;
import it.francescofiora.product.api.web.api.CategoryApi;
import it.francescofiora.product.client.config.FeignProductConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Client Service for CategoryApi.
 */
@FeignClient(name = "PRODUCT-API", contextId = "CATEGORY-SERVICE", path = "${product.path:}",
    configuration = FeignProductConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface CategoryApiService extends CategoryApi {

}
