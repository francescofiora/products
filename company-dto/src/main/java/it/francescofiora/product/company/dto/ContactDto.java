package it.francescofiora.product.company.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.product.common.dto.DtoIdentifier;
import it.francescofiora.product.common.util.DtoUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contact Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class ContactDto extends BaseContactDto implements DtoIdentifier, Serializable {

  @Serial
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

  /**
   * The id.
   */
  @Schema(description = "Unique Contact identifier", example = "1", requiredMode = REQUIRED)
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
}
