package it.francescofiora.product.company.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Base CompanyDto.
 */
@Getter
@Setter
public abstract class BaseCompanyDto {

  /**
   * The name.
   */
  @Schema(description = "Name", example = "Groupon", requiredMode = REQUIRED)
  @JsonProperty("name")
  @NotBlank
  private String name;

  /**
   * The email.
   */
  @Schema(description = "Email", example = "test@mail", requiredMode = REQUIRED)
  @JsonProperty("email")
  @NotBlank
  private String email;

  /**
   * The web.
   */
  @Schema(description = "Web", example = "www.groupon.example", requiredMode = REQUIRED)
  @JsonProperty("web")
  @NotBlank
  private String web;
}
