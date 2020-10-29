package it.francescofiora.product.service.mapper;

import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - Dto type parameter.
 * @param <E> - Entity type parameter.
 */

public interface DtoToEntityMapper<D, E> {

  E toEntity(D dto);

  List<E> toEntity(List<D> dtoList);
}
