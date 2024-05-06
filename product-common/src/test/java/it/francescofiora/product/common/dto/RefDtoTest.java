package it.francescofiora.product.common.dto;

import it.francescofiora.product.common.TestUtils;
import org.junit.jupiter.api.Test;

class RefDtoTest {

  private static final Long ID = 1L;

  private static class DummyRefDto extends RefDto {}

  @Test
  void dtoEqualsTest() {
    var dtoRef1 = new DummyRefDto();
    dtoRef1.setId(ID);
    var dtoRef2 = new DummyRefDto();
    dtoRef2.setId(ID);
    TestUtils.checkEqualHashAndToString(dtoRef1, dtoRef2);
  }
}
