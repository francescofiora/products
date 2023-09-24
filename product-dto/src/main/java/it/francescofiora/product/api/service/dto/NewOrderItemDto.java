package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

  private static final long serialVersionUID = 1L;

  /**
   * The quantity.
   */
  @Schema(description = "Quantity", example = "10", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  @NotNull
  @Positive
  private Integer quantity;

  /**
   * The product.
   */
  @Schema(requiredMode = RequiredMode.REQUIRED)
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
