package it.francescofiora.product.api.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.common.dto.DtoIdentifier;
import it.francescofiora.product.common.util.DtoUtils;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
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

  /**
   * The id.
   */
  @Schema(description = "Unique identifier", example = "1", requiredMode = REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  /**
   * The quantity.
   */
  @Schema(description = "Quantity", example = "10", requiredMode = REQUIRED)
  @JsonProperty("quantity")
  @NotNull
  @Positive
  private Integer quantity;

  /**
   * The total price.
   */
  @Schema(description = "Total Price", example = "10", requiredMode = REQUIRED)
  @JsonProperty("totalPrice")
  @NotNull
  @DecimalMin(value = "0")
  private BigDecimal totalPrice;

  /**
   * The product.
   */
  @Schema(requiredMode = REQUIRED)
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
