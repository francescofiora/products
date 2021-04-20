package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseOrderDto {

  @Schema(description = "placed Date", example = "", required = true)
  @JsonProperty("placedDate")
  @NotNull
  private Instant placedDate;

  @Schema(description = "order code", example = "ORD_1", required = true)
  @JsonProperty("code")
  @NotBlank
  private String code;

  @Schema(description = "customer", example = "Some Company Ltd", required = true)
  @JsonProperty("customer")
  @NotBlank
  private String customer;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BaseOrderDto other = (BaseOrderDto) o;
    if (placedDate == null) {
      if (other.placedDate != null) {
        return false;
      }
    } else if (!placedDate.equals(other.placedDate)) {
      return false;
    }
    if (code == null) {
      if (other.code != null) {
        return false;
      }
    } else if (!code.equals(other.code)) {
      return false;
    }
    if (customer == null) {
      if (other.customer != null) {
        return false;
      }
    } else if (!customer.equals(other.customer)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((placedDate == null) ? 0 : placedDate.hashCode());
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((customer == null) ? 0 : customer.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "BaseOrderDTO{" + ", placedDate='" + getPlacedDate() + "'" + ", code='"
        + getCode() + ", customer='" + getCustomer() + "'" + "}";
  }
}
