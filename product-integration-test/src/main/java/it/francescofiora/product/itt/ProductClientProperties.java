package it.francescofiora.product.itt;

import it.francescofiora.product.client.ClientInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Product Http Client Properties.
 */
@Data
public class ProductClientProperties implements ClientInfo {

  @NotBlank
  private String baseUrl;

  @NotBlank
  private String userName;

  @NotBlank
  private String password;

  boolean sslEnabled = false;

  private String keyStoreFile;

  private String keyStorePassword;
}
