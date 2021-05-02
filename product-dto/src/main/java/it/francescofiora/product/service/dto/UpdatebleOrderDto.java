package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.service.util.DtoUtils;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatebleOrderDto extends BaseOrderDto implements DtoIdentifier, Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Order identifier", example = "1", required = true)
  @JsonProperty("id")
  @NotNull
  private Long id;

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "UpdatebleOrderDto{" + "id=" + getId() + ", placedDate='" + getPlacedDate() + "'"
        + ", code='" + getCode() + ", customer='" + getCustomer() + "'" + "}";
  }
}
