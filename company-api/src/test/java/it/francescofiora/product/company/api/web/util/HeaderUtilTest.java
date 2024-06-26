package it.francescofiora.product.company.api.web.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class HeaderUtilTest {

  @Test
  void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {
    var constructor = HeaderUtil.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);
    constructor.newInstance();
  }
}
