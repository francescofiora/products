package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.service.util.DtoUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto extends BaseOrderDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Order identifier", example = "1", required = true)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(description = "total Price", example = "10", required = true)
  @JsonProperty("totalPrice")
  @NotNull
  private BigDecimal totalPrice;
  
  @Schema(description = "status", example = "", required = true)
  @JsonProperty("status")
  @NotNull
  private OrderStatus status;

  @Schema(required = true)
  @JsonProperty("items")
  @NotNull
  private List<OrderItemDto> items = new ArrayList<>();

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
    return "OrderDTO{" + "id=" + getId() + ", placedDate='" + getPlacedDate() + "'"
        + ", status='" + getStatus() + "'" + ", code='" + getCode() + ", customer='" + getCustomer()
        + "'" + "}";
  }
}
