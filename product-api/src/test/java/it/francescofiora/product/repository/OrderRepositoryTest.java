package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.domain.Order;
import it.francescofiora.product.util.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderRepositoryTest extends AbstractTestRepository {

  @Autowired
  private OrderRepository orderRepository;

  @Test
  public void testCrud() throws Exception {
    Order expected = TestUtils.createOrder(null);
    expected = orderRepository.save(expected);

    Optional<Order> opt = orderRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);

    orderRepository.delete(expected);
    opt = orderRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
