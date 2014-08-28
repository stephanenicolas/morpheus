package com.github.stephanenicolas.morpheus.commons;

import java.lang.reflect.Method;
import javassist.CtField;

public final class NullableUtils {
    private NullableUtils() {
    }
    
    public static boolean isNotNullable(CtField field) {
        return !isNullable( field );
    }

    public static boolean isNullable(CtField field) {
      try {
        for( Object annotation : field.getAnnotations() ) {
          Class annotionClass = annotation.getClass();

          //workaround for robolectric
          //https://github.com/robolectric/robolectric/pull/1240
          Method method = annotionClass.getMethod("annotationType");
          Class type = (Class) method.invoke(annotation);
          if( "Nullable".equals(type.getSimpleName()))
            return true;
          }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      return false;
    }
}
