package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A Dto for a new {@link it.francescofiora.product.domain.Product} entity.
 */
public class ProductDto extends BaseProductDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @Schema(required = true)
  @JsonProperty("category")
  private CategoryDto category;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotNull
  public CategoryDto getCategory() {
    return category;
  }

  public void setCategory(CategoryDto category) {
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

    ProductDto productDto = (ProductDto) o;
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
    return "ProductDto{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='"
        + getDescription() + "'" + ", price=" + getPrice() + ", size='" + getSize() + "'"
        + ", image='" + getImage() + "'" + ", category=" + getCategory() + "}";
  }
}
