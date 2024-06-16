package it.francescofiora.product.company.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * New Contact Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewContactDto extends BaseContactDto implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * The company.
   */
  @Schema(requiredMode = REQUIRED)
  @JsonProperty("company")
  @NotNull
  @Valid
  private RefCompanyDto company;

  /**
   * The address.
   */
  @Schema(requiredMode = REQUIRED)
  @JsonProperty("address")
  @NotNull
  @Valid
  private RefAddressDto address;


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var other = (NewContactDto) obj;
    return Objects.equals(getCompany(), other.getCompany())
        && Objects.equals(getAddress(), other.getAddress())
        && Objects.equals(getName(), other.getName())
        && Objects.equals(getDescription(), other.getDescription())
        && Objects.equals(getPhone(), other.getPhone())
        && Objects.equals(getEmail(), other.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAddress(), getCompany(), getName(), getDescription(), getPhone(),
        getEmail());
  }
}
