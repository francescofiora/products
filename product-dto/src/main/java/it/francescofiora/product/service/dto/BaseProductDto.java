package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.service.dto.enumeration.Size;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A base Dto for the {@link it.francescofiora.product.domain.Product} entity.
 */
public abstract class BaseProductDto {

  @Schema(description = "name", example = "SHIRTM01", required = true)
  @JsonProperty("name")
  private String name;

  @Schema(description = "description of the product", example = "Shirt for Men")
  @JsonProperty("description")
  private String description;

  @Schema(description = "price", example = "10", required = true)
  @JsonProperty("price")
  private BigDecimal price;

  @Schema(description = "size", example = "L", required = true)
  @JsonProperty("size")
  private Size size;

  @Schema(description = "image", example = "image_001.jpg")
  @JsonProperty("image")
  private String image;

  @Schema(description = "image content type", example = "jpg")
  @JsonProperty("imageContentType")
  private String imageContentType;

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

  @NotNull
  @DecimalMin(value = "0")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @NotNull
  public Size getSize() {
    return size;
  }

  public void setSize(Size size) {
    this.size = size;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getImageContentType() {
    return imageContentType;
  }

  public void setImageContentType(String imageContentType) {
    this.imageContentType = imageContentType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BaseProductDto other = (BaseProductDto) o;
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
    if (price == null) {
      if (other.price != null) {
        return false;
      }
    } else if (!price.equals(other.price)) {
      return false;
    }
    if (size == null) {
      if (other.size != null) {
        return false;
      }
    } else if (!size.equals(other.size)) {
      return false;
    }
    if (image == null) {
      if (other.image != null) {
        return false;
      }
    } else if (!image.equals(other.image)) {
      return false;
    }
    if (imageContentType == null) {
      if (other.imageContentType != null) {
        return false;
      }
    } else if (!imageContentType.equals(other.imageContentType)) {
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
    result = prime * result + ((price == null) ? 0 : price.hashCode());
    result = prime * result + ((size == null) ? 0 : size.hashCode());
    result = prime * result + ((image == null) ? 0 : image.hashCode());
    result = prime * result + ((imageContentType == null) ? 0 : imageContentType.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "BaseProductDTO{" + ", name='" + getName() + "'" + ", description='"
        + getDescription() + "'" + ", price=" + getPrice() + ", size='" + getSize() + "'"
        + ", image='" + getImage() + "'" + "}";
  }
}
