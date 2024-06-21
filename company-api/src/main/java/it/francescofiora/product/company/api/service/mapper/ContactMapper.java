package it.francescofiora.product.company.api.service.mapper;

import it.francescofiora.product.company.api.domain.Contact;
import it.francescofiora.product.company.dto.ContactDto;
import it.francescofiora.product.company.dto.NewContactDto;
import it.francescofiora.product.company.dto.UpdatebleContactDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDto}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface ContactMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "address", ignore = true)
  void updateEntityFromDto(UpdatebleContactDto entityDto, @MappingTarget Contact entity);

  @Mapping(target = "company.id", source = "address.company.id")
  ContactDto toDto(Contact entity);

  @Mapping(target = "id", ignore = true)
  Contact toEntity(NewContactDto entityDto);
}
