package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.service.util.DtoUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique identifier", example = "1", required = true)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(description = "quantity", example = "10", required = true)
  @JsonProperty("quantity")
  @NotNull
  @Positive
  private Integer quantity;

  @Schema(description = "total Price", example = "10", required = true)
  @JsonProperty("totalPrice")
  @NotNull
  @DecimalMin(value = "0")
  private BigDecimal totalPrice;

  @Schema(required = true)
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

  @Override
  public String toString() {
    return "OrderItemDto{" + "id=" + getId() + ", quantity=" + getQuantity() + ", totalPrice="
        + getTotalPrice() + ", product=" + getProduct() + "}";
  }
}
