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
public class NewProductDto extends BaseProductDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(required = true)
  @JsonProperty("category")
  @NotNull
  @Valid
  private RefCategoryDto category;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var other = (NewProductDto) obj;
    return Objects.equals(getCategory(), other.getCategory())
        && Objects.equals(getDescription(), other.getDescription())
        && Objects.equals(getImage(), other.getImage())
        && Objects.equals(getImageContentType(), other.getImageContentType())
        && Objects.equals(getName(), other.getName())
        && Objects.equals(getPrice(), other.getPrice())
        && Objects.equals(getSize(), other.getSize());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCategory(), getDescription(), getImage(), getImageContentType(),
        getName(), getPrice(), getSize());
  }

  @Override
  public String toString() {
    return "NewProductDTO{" + ", name='" + getName() + "'" + ", description='" + getDescription()
        + "'" + ", price=" + getPrice() + ", size='" + getSize() + "'" + ", image='" + getImage()
        + "'" + ", imageContentType='" + getImageContentType() + "'" + ", category=" + getCategory()
        + "}";
  }
}
