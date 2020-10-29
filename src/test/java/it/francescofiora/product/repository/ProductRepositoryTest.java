package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import it.francescofiora.product.domain.Product;

public class ProductRepositoryTest extends AbstractTestRepository {

  @Autowired
  private ProductRepository productRepository;

  @Test
  public void testCRUD() throws Exception {
    Product expected = new Product().name("name").description("description");
    expected = productRepository.save(expected);
    
    Optional<Product> opt = productRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);
    
    productRepository.delete(expected);
    opt = productRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
