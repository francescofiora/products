package it.francescofiora.product.company.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.api.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ContactRepositoryTest extends AbstractTestRepository {

  @Autowired
  private ContactRepository contactRepository;

  @Test
  void testCrud() {
    var expected = TestUtils.createContact(null);
    expected = contactRepository.save(expected);

    var opt = contactRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    contactRepository.delete(expected);
    opt = contactRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
