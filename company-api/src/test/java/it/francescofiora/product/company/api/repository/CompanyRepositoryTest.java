package it.francescofiora.product.company.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.api.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CompanyRepositoryTest extends AbstractTestRepository {

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  void testCrud() {
    var expected = TestUtils.createCompany(null);
    expected = companyRepository.save(expected);

    var opt = companyRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    companyRepository.delete(expected);
    opt = companyRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
