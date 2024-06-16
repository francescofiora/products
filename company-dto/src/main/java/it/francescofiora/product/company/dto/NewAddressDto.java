package it.francescofiora.product.company.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * New Address Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class NewAddressDto extends BaseAddressDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var other = (NewAddressDto) obj;
    return Objects.equals(getType(), other.getType())
        && Objects.equals(getAddress(), other.getAddress())
        && Objects.equals(getZipcode(), other.getZipcode())
        && Objects.equals(getCountry(), other.getCountry())
        && Objects.equals(getCurrency(), other.getCurrency())
        && Objects.equals(getTaxNumber(), other.getTaxNumber())
        && Objects.equals(getPhone(), other.getPhone())
        && Objects.equals(getEmail(), other.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getType(), getAddress(), getZipcode(), getCountry(), getCurrency(),
        getTaxNumber(), getPhone(), getEmail());
  }
}
