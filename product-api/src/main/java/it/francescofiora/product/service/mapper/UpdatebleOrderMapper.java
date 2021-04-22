package it.francescofiora.product.service.mapper;

import it.francescofiora.product.domain.Order;
import it.francescofiora.product.service.dto.UpdatebleOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Order} and its Dto {@link UpdatebleOrderDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UpdatebleOrderMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "orderItems", ignore = true)
  void updateEntityFromDto(UpdatebleOrderDto orderDto, @MappingTarget Order order);
}
