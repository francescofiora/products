package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.service.dto.enumeration.Size;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseProductDto {

  @Schema(description = "Name", example = "SHIRTM01", required = true)
  @JsonProperty("name")
  @NotBlank
  private String name;

  @Schema(description = "Description of the product", example = "Shirt for Men")
  @JsonProperty("description")
  @NotBlank
  private String description;

  @Schema(description = "Price", example = "10", required = true)
  @JsonProperty("price")
  @NotNull
  @DecimalMin(value = "0")
  private BigDecimal price;

  @Schema(description = "Size", example = "L", required = true)
  @JsonProperty("size")
  @NotNull
  private Size size;

  @Schema(description = "Image", example = "image_001.jpg")
  @JsonProperty("image")
  private String image;

  @Schema(description = "Image content type", example = "jpg")
  @JsonProperty("imageContentType")
  private String imageContentType;

}
