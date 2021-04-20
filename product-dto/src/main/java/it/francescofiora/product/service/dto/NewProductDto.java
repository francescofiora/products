package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A Dto for the {@link it.francescofiora.product.domain.Product} entity.
 */
public class NewProductDto extends BaseProductDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(required = true)
  @JsonProperty("category")
  private RefCategoryDto category;

  @NotNull
  @Valid
  public RefCategoryDto getCategory() {
    return category;
  }

  public void setCategory(RefCategoryDto category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NewProductDto other = (NewProductDto) o;
    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
    result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
    result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
    result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
    result = prime * result + ((getImage() == null) ? 0 : getImage().hashCode());
    result =
        prime * result + ((getImageContentType() == null) ? 0 : getImageContentType().hashCode());
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "NewProductDTO{" + ", name='" + getName() + "'" + ", description='" + getDescription()
        + "'" + ", price=" + getPrice() + ", size='" + getSize() + "'" + ", image='" + getImage()
        + "'" + ", imageContentType='" + getImageContentType() + "'" + ", category=" + getCategory()
        + "}";
  }
}
