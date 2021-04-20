package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrderDto extends BaseOrderDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(required = true)
  @JsonProperty("items")
  @NotEmpty
  @Valid
  private List<NewOrderItemDto> items = new ArrayList<>();

  @Override
  public String toString() {
    return "NewOrderDto{" + ", placedDate='" + getPlacedDate() + "'" + ", code='"
        + getCode() + ", customer='" + getCustomer() + "'" + "}";
  }
}
