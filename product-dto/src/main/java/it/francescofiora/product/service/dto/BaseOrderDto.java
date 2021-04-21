package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseOrderDto {

  @Schema(description = "placed Date", example = "", required = true)
  @JsonProperty("placedDate")
  @NotNull
  private Instant placedDate;

  @Schema(description = "order code", example = "ORD_1", required = true)
  @JsonProperty("code")
  @NotBlank
  private String code;

  @Schema(description = "customer", example = "Some Company Ltd", required = true)
  @JsonProperty("customer")
  @NotBlank
  private String customer;

}
