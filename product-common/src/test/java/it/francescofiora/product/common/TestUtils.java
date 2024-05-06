package it.francescofiora.product.common;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Utility for testing.
 */
public interface TestUtils {
  /**
   * assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isTrue();
    assertThat(obj1).hasSameHashCodeAs(obj2.hashCode());
    assertThat(obj1).hasToString(obj2.toString());
  }
}
