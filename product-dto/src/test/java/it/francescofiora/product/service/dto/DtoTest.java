package it.francescofiora.product.service.dto;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.product.service.util.DtoEqualsTester;
import it.francescofiora.product.service.util.FilterPackageInfo;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DtoTest {
  // Configured for expectation, so we know when a class gets added or removed.
  private static final int EXPECTED_CLASS_COUNT = 12;

  // The package to test
  private static final String DTO_PACKAGE = "it.francescofiora.product.service.dto";

  @Test
  void ensureExpectedPojoCount() {
    List<PojoClass> pojoClasses =
        PojoClassFactory.getPojoClasses(DTO_PACKAGE, new FilterPackageInfo());
    Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, pojoClasses.size());
  }

  @Test
  void testPojoStructureAndBehavior() {
    // @formatter:off
    Validator validator = ValidatorBuilder.create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .with(new SetterTester())
        .with(new GetterTester())
        .with(new DtoEqualsTester()).build();
    // @formatter:on

    validator.validate(DTO_PACKAGE, new FilterPackageInfo());
  }
}
