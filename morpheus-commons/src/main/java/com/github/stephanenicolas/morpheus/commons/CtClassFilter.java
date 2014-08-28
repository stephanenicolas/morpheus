package com.github.stephanenicolas.morpheus.commons;

import javassist.CtClass;

/**
 * Filters Javassist classes.
 * @author SNI
 */
public interface CtClassFilter {
  public boolean isValid(CtClass clazz);
}
