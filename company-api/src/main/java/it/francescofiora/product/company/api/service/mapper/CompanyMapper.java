package it.francescofiora.product.company.api.service.mapper;

import it.francescofiora.product.company.api.domain.Company;
import it.francescofiora.product.company.dto.CompanyDto;
import it.francescofiora.product.company.dto.NewCompanyDto;
import it.francescofiora.product.company.dto.UpdatebleCompanyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDto}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface CompanyMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "addresses", ignore = true)
  void updateEntityFromDto(UpdatebleCompanyDto entityDto, @MappingTarget Company entity);

  CompanyDto toDto(Company entity);

  @Mapping(target = "id", ignore = true)
  Company toEntity(NewCompanyDto entityDto);
}
