package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import it.francescofiora.product.api.service.util.DtoUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * OrderItem Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class OrderItemDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique identifier", example = "1", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(description = "Quantity", example = "10", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  @NotNull
  @Positive
  private Integer quantity;

  @Schema(description = "Total Price", example = "10", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("totalPrice")
  @NotNull
  @DecimalMin(value = "0")
  private BigDecimal totalPrice;

  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("product")
  @NotNull
  private ProductDto product;

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
