package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatebleOrderDto extends BaseOrderDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Order identifier", example = "1", required = true)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UpdatebleOrderDto orderDto = (UpdatebleOrderDto) o;
    if (orderDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), orderDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "OrderDTO{" + "id=" + getId() + ", placedDate='" + getPlacedDate() + "'" + ", code='"
        + getCode() + ", customer='" + getCustomer() + "'" + "}";
  }
}
