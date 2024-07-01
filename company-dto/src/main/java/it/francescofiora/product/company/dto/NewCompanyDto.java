package it.francescofiora.product.company.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * New Company Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewCompanyDto extends BaseCompanyDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The addresses.
   */
  @Schema(requiredMode = REQUIRED)
  @JsonProperty("addresses")
  @NotEmpty
  @Valid
  private List<NewAddressDto> addresses = new ArrayList<>();

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var other = (NewCompanyDto) obj;
    return Objects.equals(getName(), other.getName())
        && Objects.equals(getEmail(), other.getEmail())
        && Objects.equals(getWeb(), other.getWeb());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getEmail(), getWeb());
  }
}
