package it.francescofiora.product.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.service.util.TestUtils;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class NewOrderDtoTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    NewOrderDto orderDto1 = TestUtils.createNewOrderDto();
    NewOrderDto orderDto2 = new NewOrderDto();
    assertThat(orderDto1).isNotEqualTo(orderDto2);

    orderDto2 = TestUtils.createNewOrderDto();
    TestUtils.checkEqualHashAndToString(orderDto1, orderDto2);

    orderDto2.setCode("CodeDiff");
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);
    orderDto1.setCode(null);
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);

    orderDto1 = TestUtils.createNewOrderDto();
    orderDto2 = TestUtils.createNewOrderDto();
    orderDto2.setCustomer("CustomerDiff");
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);
    orderDto1.setCustomer(null);
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);
    
    orderDto1 = TestUtils.createNewOrderDto();
    orderDto2 = TestUtils.createNewOrderDto();
    orderDto2.setPlacedDate(Instant.now().plusMillis(1000));
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);
    orderDto1.setPlacedDate(null);
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);

    orderDto1 = TestUtils.createNewOrderDto();
    orderDto2 = TestUtils.createNewOrderDto();
    orderDto2.getItems().clear();
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);
    orderDto1.setItems(null);
    TestUtils.checkNotEqualHashAndToString(orderDto1, orderDto2);
  }
  
}
