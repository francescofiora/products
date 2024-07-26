package it.francescofiora.product.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * DiscoveryClient Config.
 */
@Configuration
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "API Gateway", description = "Documentation API Gateway"))
public class DiscoveryClientConfig {

}
