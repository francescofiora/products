package it.francescofiora.product.domain;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.product.util.FilterPackageInfo;
import it.francescofiora.product.util.PojoEqualsTester;
import java.util.List;
import org.junit.jupiter.api.Test;

class PojoTest {
  // Configured for expectation, so we know when a class gets added or removed.
  private static final int EXPECTED_CLASS_COUNT = 5;

  // The package to test
  private static final String POJO_PACKAGE = PojoTest.class.getPackage().getName();

  @Test
  void ensureExpectedCount() {
    List<PojoClass> classes =
        PojoClassFactory.getPojoClasses(POJO_PACKAGE, new FilterPackageInfo());
    Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, classes.size());
  }

  @Test
  void testStructureAndBehavior() {
    // @formatter:off
    Validator validator = ValidatorBuilder.create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .with(new SetterTester())
        .with(new GetterTester())
        .with(new PojoEqualsTester()).build();
    // @formatter:on

    validator.validate(POJO_PACKAGE, new FilterPackageInfo());
  }
}
