package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatebleProductDto extends BaseProductDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Product identifier", example = "1", required = true)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Schema(required = true)
  @JsonProperty("category")
  @NotNull
  @Valid
  private RefCategoryDto category;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UpdatebleProductDto productDto = (UpdatebleProductDto) o;
    if (productDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), productDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "UpdatebleProductDto{" + "id=" + getId() + ", name='" + getName() + "'"
        + ", description='" + getDescription() + "'" + ", price=" + getPrice() + ", size='"
        + getSize() + "'" + ", image='" + getImage() + "'" + ", category=" + getCategory() + "}";
  }
}
