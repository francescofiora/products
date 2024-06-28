package it.francescofiora.product.itt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application Starter.
 */
@ComponentScan(basePackages = {"it.francescofiora.product.itt",
    "it.francescofiora.product.client", "it.francescofiora.product.company.client"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"it.francescofiora.product.client",
    "it.francescofiora.product.company.client"})
@SpringBootApplication
public class Application {

  /**
   * Main method.
   *
   * @param args args
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
