package it.francescofiora.product.gateway.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Uri Configuration.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties
public class UriConfiguration {

  @Valid
  @NotNull
  private AppServiceConfig product;

  @Valid
  @NotNull
  private AppServiceConfig company;
}
