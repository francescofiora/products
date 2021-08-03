package it.francescofiora.product.repository;

import it.francescofiora.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
