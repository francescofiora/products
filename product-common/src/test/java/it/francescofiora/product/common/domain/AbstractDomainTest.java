package it.francescofiora.product.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.product.common.TestUtils;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

class AbstractDomainTest {

  private static final Long ID = 1L;

  @Getter
  @Setter
  private static class DummyDomain extends AbstractDomain {
    Long id;
  }

  @Test
  void dtoEqualsTest() {
    var domain1 = new DummyDomain();
    domain1.setId(ID);
    assertThat(domain1.equals(domain1)).isTrue();

    var domain2 = new DummyDomain();
    domain2.setId(ID);
    TestUtils.checkEqualHashAndToString(domain1, domain2);

    domain1.setId(null);
    domain2.setId(null);
    assertThat(domain1.equals(domain2)).isFalse();

    assertThat(domain1.equals(new Object())).isFalse();
  }
}
