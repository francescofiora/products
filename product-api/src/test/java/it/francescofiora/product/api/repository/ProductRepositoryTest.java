package it.francescofiora.product.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductRepositoryTest extends AbstractTestRepository {

  @Autowired
  private ProductRepository productRepository;

  @Test
  void testCrud() throws Exception {
    var expected = TestUtils.createProduct(null);
    expected = productRepository.save(expected);

    var opt = productRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    productRepository.delete(expected);
    opt = productRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
