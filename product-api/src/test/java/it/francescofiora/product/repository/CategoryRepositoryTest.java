package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Category;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryRepositoryTest extends AbstractTestRepository {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  public void testCrud() throws Exception {
    Category expected = new Category().name("name").description("description");
    expected = categoryRepository.save(expected);
    
    Optional<Category> opt = categoryRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);
    
    categoryRepository.delete(expected);
    opt = categoryRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
