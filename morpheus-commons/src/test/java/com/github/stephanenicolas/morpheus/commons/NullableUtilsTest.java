package com.github.stephanenicolas.morpheus.commons;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import org.junit.Test;

import static com.github.stephanenicolas.morpheus.commons.TestUtilityClassUtils.assertUtilityClassWellDefined;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NullableUtilsTest {

  @Test
  public void testUtilityClass() throws Exception {
    assertUtilityClassWellDefined(NullableUtils.class);
  }

  @Test
  public void testIsNullable() throws NotFoundException {
    //GIVEN
    CtClass clazz = ClassPool.getDefault().get(TestClassWithAnnotatedFields.class.getName());
    CtField fieldA = clazz.getField("a");

    //THEN
    assertThat(NullableUtils.isNullable(fieldA), is(true));
  }

  @Test
  public void testIsNotNullable() throws NotFoundException {
    //GIVEN
    CtClass clazz = ClassPool.getDefault().get(TestClassWithAnnotatedFields.class.getName());
    CtField fieldB = clazz.getField("b");

    //THEN
    assertThat(NullableUtils.isNotNullable(fieldB), is(true));
  }

  private static class TestClassWithAnnotatedFields {
    @Nullable
    private int a;
    private int b;
  }

  private @interface Nullable {
  }
}