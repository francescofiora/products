package it.francescofiora.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import it.francescofiora.product.domain.Order;

public class OrderRepositoryTest extends AbstractTestRepository {

  @Autowired
  private OrderRepository orderRepository;

  @Test
  public void testCRUD() throws Exception {
    Order expected = new Order().code("CODE").customer("Customer");
    expected = orderRepository.save(expected);
    
    Optional<Order> opt = orderRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);
    
    orderRepository.delete(expected);
    opt = orderRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }
}
