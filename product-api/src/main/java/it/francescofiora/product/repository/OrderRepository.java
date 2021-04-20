package it.francescofiora.product.repository;

import it.francescofiora.product.domain.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
