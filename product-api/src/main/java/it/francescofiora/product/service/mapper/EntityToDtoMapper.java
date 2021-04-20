package it.francescofiora.product.service.mapper;

import java.util.List;

/**
 * Contract for a generic entity to dto  mapper.
 *
 * @param <D> - Dto type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityToDtoMapper<D, E> {

  D toDto(E entity);

  List<D> toDto(List<E> entityList);
}
