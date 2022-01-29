package it.francescofiora.product.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.domain.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderItemRepositoryTest extends AbstractTestRepository {

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Test
  void testCrud() throws Exception {
    var expected = new OrderItem();
    expected = orderItemRepository.save(expected);

    var opt = orderItemRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    orderItemRepository.delete(expected);
    opt = orderItemRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
