package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A Dto for the {@link it.francescofiora.product.domain.Category} entity.
 */
public class CategoryDto extends NewCategoryDto {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @NotNull
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CategoryDto categoryDto = (CategoryDto) o;
    if (categoryDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), categoryDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "CategoryDto{" + "id=" + getId() + ", name='" + getName() + "'"
        + ", description='" + getDescription() + "'" + "}";
  }
}
