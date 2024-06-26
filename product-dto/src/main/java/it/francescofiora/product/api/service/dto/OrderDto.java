package it.francescofiora.product.api.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.api.service.dto.enumeration.OrderStatus;
import it.francescofiora.product.common.dto.DtoIdentifier;
import it.francescofiora.product.common.util.DtoUtils;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
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

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The id.
   */
  @Schema(description = "Unique Order identifier", example = "1", requiredMode = REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  /**
   * The total price.
   */
  @Schema(description = "Total Price", example = "10", requiredMode = REQUIRED)
  @JsonProperty("totalPrice")
  @NotNull
  private BigDecimal totalPrice;

  /**
   * The status.
   */
  @Schema(description = "Status", example = "", requiredMode = REQUIRED)
  @JsonProperty("status")
  @NotNull
  private OrderStatus status;

  /**
   * The items.
   */
  @Schema(requiredMode = REQUIRED)
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
