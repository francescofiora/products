package it.francescofiora.product.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RefDto {

  @Schema(description = "Unique identifier", example = "1", required = true)
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

    RefDto authorDto = (RefDto) o;
    if (authorDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), authorDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
  
}
