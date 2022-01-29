package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * BaseOrder Dto.
 */
@Getter
@Setter
public abstract class BaseOrderDto {

  @Schema(description = "Placed Date", example = "", required = true)
  @JsonProperty("placedDate")
  @NotNull
  private Instant placedDate;

  @Schema(description = "Order code", example = "ORD_1", required = true)
  @JsonProperty("code")
  @NotBlank
  private String code;

  @Schema(description = "Customer", example = "Some Company Ltd", required = true)
  @JsonProperty("customer")
  @NotBlank
  private String customer;

}
