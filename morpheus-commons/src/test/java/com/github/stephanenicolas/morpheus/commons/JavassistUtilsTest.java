package com.github.stephanenicolas.morpheus.commons;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JavassistUtilsTest {

  private CtClass testClass;

  @Before
  public void setup() throws NotFoundException {
    testClass = ClassPool.getDefault().get(TestClass.class.getName());
  }

  @After
  public void tearDown() throws NotFoundException {
    testClass.detach();
  }

  @Test
  public void testUtilityClass() throws Exception {
    assertUtilityClassWellDefined(JavassistUtils.class);
  }

  @Test
  public void testIsActivity() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(Activity.class.getName());
    assertThat(JavassistUtils.isActivity(clazz), is(true));
  }

  @Test
  public void testIsService() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(Service.class.getName());
    assertThat(JavassistUtils.isService(clazz), is(true));
  }

  @Test
  public void testIsApplication() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(Application.class.getName());
    assertThat(JavassistUtils.isApplication(clazz), is(true));
  }

  @Test
  public void testIsContentProvider() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(ContentProvider.class.getName());
    assertThat(JavassistUtils.isContentProvider(clazz), is(true));
  }

  @Test
  public void testIsBroadcastReceiver() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(BroadcastReceiver.class.getName());
    assertThat(JavassistUtils.isBroadCastReceiver(clazz), is(true));
  }

  @Test
  public void testIsFragment() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(Fragment.class.getName());
    assertThat(JavassistUtils.isFragment(clazz), is(true));
  }

  @Test
  public void testIsSupportFragment() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(android.support.v4.app.Fragment.class.getName());
    assertThat(JavassistUtils.isSupportFragment(clazz), is(true));
  }

  @Test
  public void testIsView() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(View.class.getName());
    assertThat(JavassistUtils.isView(clazz), is(true));
  }

  @Test
  public void testIsContext() throws NotFoundException {
    CtClass clazz = ClassPool.getDefault().get(Context.class.getName());
    assertThat(JavassistUtils.isContext(clazz), is(true));
  }

  @Test
  public void testIsSubclass_returnTrue() throws NotFoundException {
    ClassPool classPool = ClassPool.getDefault();
    assertThat(JavassistUtils.isSubType(classPool, classPool.get(Bar.class.getName()), Foo.class),
        is(true));
  }

  @Test
  public void testIsSubclass_returnFalse() throws NotFoundException {
    ClassPool classPool = ClassPool.getDefault();
    assertThat(JavassistUtils.isSubType(classPool, classPool.get(Foo.class.getName()), Bar.class),
        is(false));
  }

  @Test(expected = NotFoundException.class)
  public void testIsSubclass_shouldThrowExceptionWhenClassNotFound() throws NotFoundException {
    ClassPool classPool = ClassPool.getDefault();
    assertThat(JavassistUtils.isSubType(classPool, classPool.get("BadClass"), Foo.class), is(true));
  }

  @Test
  public void testIsBooleanArray() throws Exception {
    CtField field = CtField.make("boolean[] f;", testClass);
    assertThat(JavassistUtils.isBooleanArray(field), is(true));
  }

  @Test
  public void testIsCharArray() throws Exception {
    CtField field = CtField.make("char[] f;", testClass);
    assertThat(JavassistUtils.isCharArray(field), is(true));
  }

  @Test
  public void testIsIntArray() throws Exception {
    CtField field = CtField.make("int[] f;", testClass);
    assertThat(JavassistUtils.isIntArray(field), is(true));
  }

  @Test
  public void testIsByteArray() throws Exception {
    CtField field = CtField.make("byte[] f;", testClass);
    assertThat(JavassistUtils.isByteArray(field), is(true));
  }

  @Test
  public void testIsLongArray() throws Exception {
    CtField field = CtField.make("long[] f;", testClass);
    assertThat(JavassistUtils.isLongArray(field), is(true));
  }

  @Test
  public void testIsShortArray() throws Exception {
    CtField field = CtField.make("short[] f;", testClass);
    assertThat(JavassistUtils.isShortArray(field), is(true));
  }

  @Test
  public void testIsDoubleArray() throws Exception {
    CtField field = CtField.make("double[] f;", testClass);
    assertThat(JavassistUtils.isDoubleArray(field), is(true));
  }

  @Test
  public void testIsFloatArray() throws Exception {
    CtField field = CtField.make("float[] f;", testClass);
    assertThat(JavassistUtils.isFloatArray(field), is(true));
  }

  @Test
  public void testIsStringArray() throws Exception {
    CtField field = CtField.make("String[] f;", testClass);
    assertThat(JavassistUtils.isStringArray(field, ClassPool.getDefault()), is(true));
  }

  @Test
  public void testIsCharSequenceArray() throws Exception {
    CtField field = CtField.make("StringBuffer[] f;", testClass);
    assertThat(JavassistUtils.isCharSequenceArray(field, ClassPool.getDefault()), is(true));
  }

  @Test
  public void testIsParcelableArray() throws Exception {
    CtField field = CtField.make("android.graphics.Point[] f;", testClass);
    assertThat(JavassistUtils.isParcelableArray(field, ClassPool.getDefault()), is(true));
  }

  @Test
  public void testIsSerializableArray() throws Exception {
    CtField field = CtField.make("String[] f;", testClass);
    assertThat(JavassistUtils.isSerializableArray(field, ClassPool.getDefault()), is(true));
  }

  @Test
  public void testIsArrayOf_returnTrue() throws Exception {
    ClassPool classPool = ClassPool.getDefault();
    CtField field = CtField.make("Integer[] f;", testClass);
    assertThat(JavassistUtils.isArrayOf(field, classPool, Number.class), is(true));
  }

  @Test
  public void testIsArrayOf_returnFalseWithBadArrayType() throws Exception {
    CtField field = CtField.make("String[] f;", testClass);
    assertThat(JavassistUtils.isArrayOf(field, ClassPool.getDefault(), Foo.class), is(false));
  }

  @Test
  public void testIsArrayOf_returnFalseWithBadType() throws Exception {
    CtField field = CtField.make("String f;", testClass);
    assertThat(JavassistUtils.isArrayOf(field, ClassPool.getDefault(), Foo.class), is(false));
  }

  @Test
  public void testIsArrayOf_returnFalseWithBadArrayType2() throws Exception {
    CtField field = CtField.make("String[] f;", testClass);
    assertThat(JavassistUtils.isArrayOf(field, CtClass.intType), is(false));
  }

  @Test
  public void testIsArrayOf_returnFalseWithBadType2() throws Exception {
    CtField field = CtField.make("int f;", testClass);
    assertThat(JavassistUtils.isArrayOf(field, CtClass.intType), is(false));
  }

  @Test(expected = NotFoundException.class)
  public void testIsArrayOf_shouldThrowExceptionWhenClassNotFound() throws Exception {
    CtField field = CtField.make("String[] f;", testClass);
    JavassistUtils.isArrayOf(field, new ClassPool(), Foo.class);
  }

  @Test
  public void testIsArrayList() throws Exception {
    CtField field = CtField.make("java.util.ArrayList f;", testClass);
    JavassistUtils.isArrayList(field, ClassPool.getDefault());
  }

  @Test
  public void testGetAllInjectedFieldsForAnnotation() throws Exception {
    Class<TestClassWithAnnotatedFields> aClass =
        TestClassWithAnnotatedFields.class;
    List<CtField> allInjectedFieldsForAnnotation1 = JavassistUtils.getAllInjectedFieldsForAnnotation(
        ClassPool.getDefault().get(aClass.getName()), Annotation1.class);
    assertThat(allInjectedFieldsForAnnotation1.size(), is(1));
    assertThat(allInjectedFieldsForAnnotation1.get(0).getName(), is("a"));
    assertThat(allInjectedFieldsForAnnotation1.get(0).getType(), is(CtClass.intType));

    List<CtField> allInjectedFieldsForAnnotation2 = JavassistUtils.getAllInjectedFieldsForAnnotation(
        ClassPool.getDefault().get(aClass.getName()), Annotation2.class);
    assertThat(allInjectedFieldsForAnnotation2.size(), is(0));
  }

  private static class Foo {
  }

  private static class Bar extends Foo {
  }

  private static class TestClass {
  }

  private static class TestClassWithAnnotatedFields {
    @Annotation1
    private int a;
    private int b;
    private int c;
  }

  private @interface Annotation1 {}

  private @interface Annotation2 {}

  /**
   * Verifies that a utility class is well defined.
   * From http://stackoverflow.com/a/10872497/693752
   * @param clazz utility class to verify.
   */
  public static void assertUtilityClassWellDefined(final Class<?> clazz)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
      IllegalAccessException {
    assertTrue("Class must be final", Modifier.isFinal(clazz.getModifiers()));
    assertEquals("There must be only one constructor", 1, clazz.getDeclaredConstructors().length);
    final Constructor<?> constructor = clazz.getDeclaredConstructor();
    if (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers())) {
      Assert.fail("Constructor is not private");
    }
    constructor.setAccessible(true);
    constructor.newInstance();
    constructor.setAccessible(false);
    for (final Method method : clazz.getMethods()) {
      if (!Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(clazz)) {
        Assert.fail("There exists a non-static method: " + method);
      }
    }
  }
}