package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderRepositoryTest extends AbstractTestRepository {

  @Autowired
  private OrderRepository orderRepository;

  @Test
  void testCrud() throws Exception {
    var expected = TestUtils.createOrder(null);
    expected = orderRepository.save(expected);

    var opt = orderRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    orderRepository.delete(expected);
    opt = orderRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
