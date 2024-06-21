package it.francescofiora.product.company.api.service.mapper;

import it.francescofiora.product.company.api.domain.Address;
import it.francescofiora.product.company.dto.AddressDto;
import it.francescofiora.product.company.dto.NewAddressDto;
import it.francescofiora.product.company.dto.RefAddressDto;
import it.francescofiora.product.company.dto.UpdatebleAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AddressMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "contacts", ignore = true)
  void updateEntityFromDto(UpdatebleAddressDto entityDto, @MappingTarget Address entity);

  AddressDto toDto(Address entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "contacts", ignore = true)
  Address toEntity(NewAddressDto entityDto);

  @Mapping(target = "type", ignore = true)
  @Mapping(target = "address", ignore = true)
  @Mapping(target = "zipcode", ignore = true)
  @Mapping(target = "country", ignore = true)
  @Mapping(target = "currency", ignore = true)
  @Mapping(target = "taxNumber", ignore = true)
  @Mapping(target = "phone", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "contacts", ignore = true)
  Address toEntity(RefAddressDto entityDto);
}
