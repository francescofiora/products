package it.francescofiora.product.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * NewOrder Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewOrderDto extends BaseOrderDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(required = true)
  @JsonProperty("items")
  @NotEmpty
  @Valid
  private List<NewOrderItemDto> items = new ArrayList<>();

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var other = (NewOrderDto) obj;
    return Objects.equals(getPlacedDate(), other.getPlacedDate())
        && Objects.equals(getCode(), other.getCode())
        && Objects.equals(getCustomer(), other.getCustomer())
        && Objects.equals(getItems(), other.getItems());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCode(), getCustomer(), getItems(), getPlacedDate());
  }
}
