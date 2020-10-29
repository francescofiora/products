package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 * A Dto for a new {@link it.francescofiora.product.domain.Category} entity.
 */
public class NewCategoryDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "name", example = "Shirt", required = true)
  @JsonProperty("name")
  private String name;

  @Schema(description = "description of the category", example = "Shirt")
  @JsonProperty("description")
  private String description;

  @NotBlank
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NewCategoryDto other = (NewCategoryDto) o;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "NewCategoryDTO{" + ", name='" + getName() + "'"
        + ", description='" + getDescription() + "'" + "}";
  }
}
