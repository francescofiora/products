package it.francescofiora.product.api.service.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * BaseProduct Dto.
 */
@Getter
@Setter
public abstract class BaseProductDto {

  /**
   * The name.
   */
  @Schema(description = "Name", example = "SHIRTM01", requiredMode = REQUIRED)
  @JsonProperty("name")
  @NotBlank
  private String name;

  /**
   * The description.
   */
  @Schema(description = "Description of the product", example = "Shirt for Men")
  @JsonProperty("description")
  @NotBlank
  private String description;

  @Schema(description = "Price", example = "10", requiredMode = REQUIRED)
  @JsonProperty("price")
  @NotNull
  @DecimalMin(value = "0")
  private BigDecimal price;

  @Schema(description = "Size", example = "L", requiredMode = REQUIRED)
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
