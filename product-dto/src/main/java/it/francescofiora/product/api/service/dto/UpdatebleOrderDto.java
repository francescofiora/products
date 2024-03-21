package it.francescofiora.product.api.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.api.service.util.DtoUtils;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * UpdatebleOrder Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class UpdatebleOrderDto extends BaseOrderDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * the id.
   */
  @Schema(description = "Unique Order identifier", example = "1", requiredMode = REQUIRED)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
