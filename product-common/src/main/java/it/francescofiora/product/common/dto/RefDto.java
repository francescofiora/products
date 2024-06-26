package it.francescofiora.product.common.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.common.util.DtoUtils;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Ref Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public abstract class RefDto implements DtoIdentifier {

  /**
   * the id.
   */
  @Schema(description = "Unique identifier", example = "1", requiredMode = REQUIRED)
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
