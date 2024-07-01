package it.francescofiora.product.api.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NewOrderItem Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewOrderItemDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The quantity.
   */
  @Schema(description = "Quantity", example = "10", requiredMode = REQUIRED)
  @JsonProperty("quantity")
  @NotNull
  @Positive
  private Integer quantity;

  /**
   * The product.
   */
  @Schema(requiredMode = REQUIRED)
  @JsonProperty("product")
  @NotNull
  @Valid
  private RefProductDto product;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var other = (NewOrderItemDto) obj;
    return Objects.equals(getQuantity(), other.getQuantity())
        && Objects.equals(getProduct(), other.getProduct());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getQuantity(), getProduct());
  }
}
