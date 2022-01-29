package it.francescofiora.product.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRepositoryTest extends AbstractTestRepository {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void testCrud() throws Exception {
    var expected = TestUtils.createCategory(null);
    expected = categoryRepository.save(expected);

    var opt = categoryRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    categoryRepository.delete(expected);
    opt = categoryRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
