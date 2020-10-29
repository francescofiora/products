package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * A Dto for a new {@link it.francescofiora.product.domain.Order} entity.
 */
public class NewOrderDto extends BaseOrderDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(required = true)
  @JsonProperty("items")
  private List<NewOrderItemDto> items = new ArrayList<>();

  @NotEmpty
  @Valid
  public List<NewOrderItemDto> getItems() {
    return items;
  }

  public void setItems(List<NewOrderItemDto> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "NewOrderDto{" + ", placedDate='" + getPlacedDate() + "'" + ", code='"
        + getCode() + ", customer='" + getCustomer() + "'" + "}";
  }
}
