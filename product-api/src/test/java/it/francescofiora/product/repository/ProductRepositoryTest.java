package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Product;
import it.francescofiora.product.util.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductRepositoryTest extends AbstractTestRepository {

  @Autowired
  private ProductRepository productRepository;

  @Test
  public void testCrud() throws Exception {
    Product expected = TestUtils.createProduct(null);
    expected = productRepository.save(expected);

    Optional<Product> opt = productRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    productRepository.delete(expected);
    opt = productRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
