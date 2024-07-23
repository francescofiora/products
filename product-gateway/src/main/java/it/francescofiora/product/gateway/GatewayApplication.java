package it.francescofiora.product.gateway;

import it.francescofiora.product.gateway.config.UriConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Application Starter.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(UriConfiguration.class)
public class GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
