package it.francescofiora.product.company.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Base ContactDto.
 */
@Getter
@Setter
public abstract class BaseContactDto {

  /**
   * The name.
   */
  @Schema(description = "Name", example = "John Smith", requiredMode = REQUIRED)
  @JsonProperty("name")
  @NotBlank
  private String name;

  /**
   * The description.
   */
  @Schema(description = "Description of the contact", example = "Director", requiredMode = REQUIRED)
  @JsonProperty("description")
  @NotBlank
  private String description;

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
