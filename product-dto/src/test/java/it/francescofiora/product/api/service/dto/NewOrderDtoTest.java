package it.francescofiora.product.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.api.service.util.TestUtils;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class NewOrderDtoTest {

  @Test
  void dtoEqualsVerifier() {
    var orderDto1 = TestUtils.createNewOrderDto();
    var orderDto2 = new NewOrderDto();
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
