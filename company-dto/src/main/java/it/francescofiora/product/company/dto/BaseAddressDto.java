package it.francescofiora.product.company.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.company.dto.enumeration.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Base Address Dto.
 */
@Getter
@Setter
public abstract class BaseAddressDto {

  /**
   * The address type.
   */
  @Schema(description = "Type", example = "HEADQUARTER", requiredMode = REQUIRED)
  @JsonProperty("type")
  @NotNull
  private AddressType type;

  /**
   * The address.
   */
  @Schema(description = "Address", example = "My Street, Kingston, New York",
      requiredMode = REQUIRED)
  @JsonProperty("address")
  @NotBlank
  private String address;

  /**
   * The zipcode.
   */
  @Schema(description = "zipCode", example = "12401", requiredMode = REQUIRED)
  @JsonProperty("zipcode")
  @NotBlank
  private String zipcode;

  /**
   * The country.
   */
  @Schema(description = "Country", example = "US", requiredMode = REQUIRED)
  @JsonProperty("country")
  @NotBlank
  private String country;

  /**
   * The currency.
   */
  @Schema(description = "Currency", example = "USD", requiredMode = REQUIRED)
  @JsonProperty("currency")
  @NotBlank
  private String currency;

  /**
   * The tax number.
   */
  @Schema(description = "Tax Number", example = "XX-XXXXXXX", requiredMode = REQUIRED)
  @JsonProperty("tax_number")
  @NotBlank
  private String taxNumber;

  /**
   * The phone.
   */
  @Schema(description = "Phone", example = "(555) 555-1234", requiredMode = REQUIRED)
  @JsonProperty("phone")
  @NotBlank
  private String phone;

  /**
   * The email.
   */
  @Schema(description = "Email", example = "test@mail", requiredMode = REQUIRED)
  @JsonProperty("email")
  @NotBlank
  private String email;
}
