package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.OrderItem;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderItemRepositoryTest extends AbstractTestRepository {

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Test
  void testCrud() throws Exception {
    OrderItem expected = new OrderItem();
    expected = orderItemRepository.save(expected);

    Optional<OrderItem> opt = orderItemRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    orderItemRepository.delete(expected);
    opt = orderItemRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
