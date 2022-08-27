package it.francescofiora.product.itt;

import it.francescofiora.product.client.ClientInfo;
import javax.validation.constraints.NotBlank;
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
}
