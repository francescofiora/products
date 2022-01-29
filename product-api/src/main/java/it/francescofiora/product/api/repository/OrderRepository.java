package it.francescofiora.product.api.repository;

import it.francescofiora.product.api.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
