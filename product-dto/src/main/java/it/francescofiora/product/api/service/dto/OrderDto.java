package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.api.service.util.DtoUtils;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Order Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class OrderDto extends BaseOrderDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * The id.
   */
  @Schema(description = "Unique Order identifier", example = "1",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  /**
   * The total price.
   */
  @Schema(description = "Total Price", example = "10", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("totalPrice")
  @NotNull
  private BigDecimal totalPrice;

  /**
   * The status.
   */
  @Schema(description = "Status", example = "", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("status")
  @NotNull
  private OrderStatus status;

  /**
   * The items.
   */
  @Schema(requiredMode = RequiredMode.REQUIRED)
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
}
