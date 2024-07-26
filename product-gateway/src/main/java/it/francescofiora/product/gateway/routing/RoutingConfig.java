package it.francescofiora.product.gateway.routing;

import it.francescofiora.product.gateway.config.UriConfiguration;
import it.francescofiora.product.gateway.filter.CompanyFilter;
import it.francescofiora.product.gateway.filter.ProductFilter;
import jakarta.ws.rs.HttpMethod;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Routing Configuration.
 */
@Configuration
public class RoutingConfig {

  /**
   * Create a custom RouteLocator.
   *
   * @param builder the RouteLocatorBuilder
   * @param uriConfiguration the uris Configuration
   * @return the RouteLocator
   */
  @Bean
  public RouteLocator customRouteLocator(
      RouteLocatorBuilder builder, UriConfiguration uriConfiguration,
      ProductFilter productFilter, CompanyFilter companyFilter) {
    return builder.routes()
        .route("product", p -> p
            .path(
                "/api/v1/categories/**",
                "/api/v1/orders/**",
                "/api/v1/products/**")
            .filters(f -> f.filters(productFilter))
            .uri(uriConfiguration.getProduct().getUri()))
        .route("product-doc", p -> p
            .path("/product-doc/v3/api-docs")
            .and().method(HttpMethod.GET)
            .filters(f -> f.stripPrefix(1)
                .filters(productFilter))
            .uri(uriConfiguration.getProduct().getUri()))
        .route("company", p -> p
            .path(
                "/api/v1/companies/**",
                "/api/v1/contacts/**")
            .filters(f -> f.filters(companyFilter))
            .uri(uriConfiguration.getCompany().getUri()))
        .route("company-doc", p -> p
            .path("/company-doc/v3/api-docs")
            .and().method(HttpMethod.GET)
            .filters(f -> f.stripPrefix(1)
                .filters(companyFilter))
            .uri(uriConfiguration.getCompany().getUri()))
        .build();
  }
}
