package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import it.francescofiora.product.api.service.util.DtoUtils;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Product Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class ProductDto extends BaseProductDto  implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * The id.
   */
  @Schema(description = "Unique identifier", example = "1", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  /**
   * The category.
   */
  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("category")
  @NotNull
  private CategoryDto category;

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
