package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Category;
import it.francescofiora.product.util.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRepositoryTest extends AbstractTestRepository {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void testCrud() throws Exception {
    Category expected = TestUtils.createCategory(null);
    expected = categoryRepository.save(expected);

    Optional<Category> opt = categoryRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    categoryRepository.delete(expected);
    opt = categoryRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
