package it.francescofiora.product.company.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.company.api.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AddressRepositoryTest extends AbstractTestRepository {

  @Autowired
  private AddressRepository addressRepository;

  @Test
  void testCrud() {
    var expected = TestUtils.createAddress(null);
    expected = addressRepository.save(expected);

    var opt = addressRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    addressRepository.delete(expected);
    opt = addressRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
