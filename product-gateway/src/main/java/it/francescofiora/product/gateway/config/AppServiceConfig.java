package it.francescofiora.product.gateway.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * Applications Service Config.
 */
@Getter
@Setter
@Validated
public class AppServiceConfig {

  @NotBlank
  private String userName = "user";

  @NotBlank
  private String password = "password";

  @NotBlank
  private String uri = "lb://application-name";
}
