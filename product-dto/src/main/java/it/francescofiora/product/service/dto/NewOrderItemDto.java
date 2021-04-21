package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrderItemDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "quantity", example = "10", required = true)
  @JsonProperty("quantity")
  @NotNull
  @Positive
  private Integer quantity;

  @Schema(required = true)
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

    NewOrderItemDto other = (NewOrderItemDto) obj;
    return Objects.equals(getQuantity(), other.getQuantity())
        && Objects.equals(getProduct(), other.getProduct());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getQuantity(), getProduct());
  }

  @Override
  public String toString() {
    return "NewOrderItemDto{" + "quantity=" + getQuantity() + ", product=" + getProduct() + "}";
  }
}
