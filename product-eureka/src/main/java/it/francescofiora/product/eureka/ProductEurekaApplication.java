package it.francescofiora.product.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Application Starter.
 */
@EnableEurekaServer
@SpringBootApplication
public class ProductEurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductEurekaApplication.class, args);
  }
}
