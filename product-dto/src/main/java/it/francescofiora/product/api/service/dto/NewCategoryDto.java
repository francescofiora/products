package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NewCategory Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewCategoryDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * The name.
   */
  @Schema(description = "Name", example = "Shirt", requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("name")
  @NotBlank
  private String name;

  /**
   * The description.
   */
  @Schema(description = "Description of the category", example = "Shirt",
      requiredMode = RequiredMode.REQUIRED)
  @JsonProperty("description")
  @NotBlank
  private String description;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var other = (NewCategoryDto) obj;
    return Objects.equals(getName(), other.getName())
        && Objects.equals(getDescription(), other.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getDescription());
  }
}
