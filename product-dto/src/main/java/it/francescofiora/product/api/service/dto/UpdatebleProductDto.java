package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import it.francescofiora.product.api.service.util.DtoUtils;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * UpdatebleProduct Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class UpdatebleProductDto extends BaseProductDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Product identifier", example = "1",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("category")
  @NotNull
  @Valid
  private RefCategoryDto category;

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
