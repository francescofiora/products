package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * A Dto for the {@link it.francescofiora.product.domain.OrderItem} entity.
 */
public class OrderItemDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @Schema(description = "quantity", example = "10", required = true)
  @JsonProperty("quantity")
  private Integer quantity;

  @Schema(description = "total Price", example = "10", required = true)
  @JsonProperty("totalPrice")
  private BigDecimal totalPrice;

  @Schema(required = true)
  @JsonProperty("product")
  private ProductDto product;

  @NotNull
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotNull
  @Positive
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @NotNull
  @DecimalMin(value = "0")
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  @NotNull
  public ProductDto getProduct() {
    return product;
  }

  public void setProduct(ProductDto product) {
    this.product = product;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OrderItemDto orderItemDto = (OrderItemDto) o;
    if (orderItemDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), orderItemDto.getId());
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
