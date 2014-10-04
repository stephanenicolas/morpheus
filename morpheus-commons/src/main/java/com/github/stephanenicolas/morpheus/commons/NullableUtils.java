package com.github.stephanenicolas.morpheus.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javassist.CtField;
import javassist.NotFoundException;

public final class NullableUtils {
  private NullableUtils() {
  }

  public static boolean isNotNullable(CtField field) {
    return !isNullable(field);
  }

  public static boolean isNullable(CtField field) {
    try {
      for (Object annotation : field.getAnnotations()) {

        Class annotationClass = Annotation.class;

        //workaround for robolectric
        //https://github.com/robolectric/robolectric/pull/1240
        Method method = annotationClass.getMethod("annotationType");
        Class type = (Class) method.invoke(annotation);
        if ("Nullable".equals(type.getSimpleName())) return true;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return false;
  }
}
