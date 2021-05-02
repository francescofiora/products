package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCategoryDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "name", example = "Shirt", required = true)
  @JsonProperty("name")
  @NotBlank
  private String name;

  @Schema(description = "description of the category", example = "Shirt", required = true)
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

    NewCategoryDto other = (NewCategoryDto) obj;
    return Objects.equals(getName(), other.getName())
        && Objects.equals(getDescription(), other.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getDescription());
  }

  @Override
  public String toString() {
    return "NewCategoryDTO{" + ", name='" + getName() + "'" + ", description='" + getDescription()
        + "'" + "}";
  }
}
