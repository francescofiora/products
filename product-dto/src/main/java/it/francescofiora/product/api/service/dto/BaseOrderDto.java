package it.francescofiora.product.api.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * BaseOrder Dto.
 */
@Getter
@Setter
public abstract class BaseOrderDto {

  @Schema(description = "Placed Date", example = "", requiredMode = REQUIRED)
  @JsonProperty("placedDate")
  @NotNull
  private Instant placedDate;

  @Schema(description = "Order code", example = "ORD_1", requiredMode = REQUIRED)
  @JsonProperty("code")
  @NotBlank
  private String code;

  @Schema(description = "Customer", example = "Some Company Ltd",
      requiredMode = REQUIRED)
  @JsonProperty("customer")
  @NotBlank
  private String customer;

}
