package com.github.stephanenicolas.morpheus.commons;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.NotFoundException;

public final class JavassistUtils {

  private JavassistUtils() {
  }

  public static boolean isArrayOf(CtField field, CtClass typeOfArrayElements)
      throws NotFoundException {
    return field.getType().isArray() && field.getType()
        .getComponentType()
        .subtypeOf(typeOfArrayElements);
  }

  public static boolean isArrayOf(CtField field, ClassPool classPool, Class<?> superClass)
      throws NotFoundException {
    return field.getType().isArray() && isSubType(classPool, field.getType().getComponentType(),
        superClass);
  }

  public static boolean isBooleanArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.booleanType);
  }

  public static boolean isByteArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.byteType);
  }

  public static boolean isCharArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.charType);
  }

  public static boolean isDoubleArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.doubleType);
  }

  public static boolean isFloatArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.floatType);
  }

  public static boolean isIntArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.intType);
  }

  public static boolean isLongArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.longType);
  }

  public static boolean isShortArray(CtField field) throws NotFoundException {
    return isArrayOf(field, CtClass.shortType);
  }

  public static boolean isCharSequenceArray(CtField field, ClassPool classPool)
      throws NotFoundException {
    return isArrayOf(field, classPool, CharSequence.class);
  }

  public static boolean isStringArray(CtField field, ClassPool classPool) throws NotFoundException {
    return isArrayOf(field, classPool, String.class);
  }

  public static boolean isArrayList(CtField field, ClassPool classPool) throws NotFoundException {
    return isSubClass(classPool, field.getType(), ArrayList.class);
  }

  public static boolean isParcelableArray(CtField field, ClassPool classPool)
      throws NotFoundException {
    return isArrayOf(field, classPool, Parcelable.class);
  }

  public static boolean isSerializableArray(CtField field, ClassPool classPool)
      throws NotFoundException {
    return isArrayOf(field, classPool, Serializable.class);
  }

  public static boolean isActivity(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, Activity.class);
  }

  public static boolean isService(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, Service.class);
  }

  public static boolean isContext(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, Context.class);
  }

  public static boolean isBroadCastReceiver(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, BroadcastReceiver.class);
  }

  public static boolean isContentProvider(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, ContentProvider.class);
  }

  public static boolean isApplication(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, Application.class);
  }

  public static boolean isFragment(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, Fragment.class);
  }

  public static boolean isSupportFragment(CtClass clazz) throws NotFoundException {
    try {
      return isSubClass(clazz.getClassPool(), clazz, "android.support.v4.app.Fragment");
    } catch (Exception e) {
      //this can happen if support is not present
      //the cause of the null pointer exception has no meaning for me !!
      return false;
    }
  }

  public static boolean isView(CtClass clazz) throws NotFoundException {
    return isSubClass(clazz.getClassPool(), clazz, View.class);
  }

  /** Direct super class */
  public static boolean isSubType(ClassPool classPool, CtClass clazz, Class<?> superClass)
      throws NotFoundException {
    try {
      return clazz.subtypeOf(classPool.get(superClass.getName()));
    } catch (RuntimeException e) {
      //may happen with classpool issued from primitives
      return false;
    }
  }

  /** Super class */
  public static boolean isSubClass(ClassPool classPool, CtClass clazz, Class<?> superClass)
      throws NotFoundException {
    return clazz.subclassOf(classPool.get(superClass.getName()));
  }

  /** Super class */
  public static boolean isSubClass(ClassPool classPool, CtClass clazz, String superClassName)
      throws NotFoundException {
    return clazz.subclassOf(classPool.get(superClassName));
  }

  public static List<CtField> getAllInjectedFieldsForAnnotation(CtClass clazz,
      Class<? extends Annotation> annotationClazz) {
    List<CtField> result = new ArrayList<CtField>();
    CtField[] allFields = clazz.getDeclaredFields();
    for (CtField field : allFields) {
      if (field.hasAnnotation(annotationClazz)) {
        result.add(field);
      }
    }
    return result;
  }

  public static List<CtConstructor> extractValidConstructors(final CtClass classToTransform,
      CtClassFilter filter) throws NotFoundException {
    List<CtConstructor> constructors = new ArrayList<CtConstructor>();
    CtConstructor[] declaredConstructors = classToTransform.getDeclaredConstructors();
    for (CtConstructor constructor : declaredConstructors) {
      CtClass[] paramClasses = constructor.getParameterTypes();
      if (paramClasses.length >= 1) {
        int indexValidParam = findValidParamIndex(paramClasses, filter);
        if (indexValidParam >= 0) {
          constructors.add(constructor);
        }
      }
    }
    return constructors;
  }

  public static int findValidParamIndex(CtClass[] parameterTypes, CtClassFilter filter) throws NotFoundException {
    int indexParam = 0;
    for (CtClass paramClass : parameterTypes) {
      if (filter.isValid(paramClass)) {
        return indexParam;
      } else {
        indexParam++;
      }
    }
    return -1;
  }
}