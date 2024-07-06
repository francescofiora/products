package it.francescofiora.product.client;

import feign.Headers;
import it.francescofiora.product.api.web.api.OrderApi;
import it.francescofiora.product.client.config.FeignProductConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Client Service for OrderApi.
 */
@FeignClient(name = "PRODUCT-API", contextId = "ORDER-SERVICE", path = "${product.path:}",
    configuration = FeignProductConfiguration.class)
@Headers({"Content-type", "application/json"})
public interface OrderApiService extends OrderApi {

}
