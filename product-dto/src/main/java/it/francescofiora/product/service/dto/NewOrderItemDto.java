package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NewOrderItemDto other = (NewOrderItemDto) o;
    if (quantity == null) {
      if (other.quantity != null) {
        return false;
      }
    } else if (!quantity.equals(other.quantity)) {
      return false;
    }

    if (product == null) {
      if (other.product != null) {
        return false;
      }
    } else if (!product.equals(other.product)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
    result = prime * result + ((product == null) ? 0 : product.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "NewOrderItemDto{" + "quantity=" + getQuantity() + ", product=" + getProduct() + "}";
  }
}
