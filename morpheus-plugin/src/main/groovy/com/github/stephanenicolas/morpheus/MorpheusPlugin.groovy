package com.github.stephanenicolas.morpheus;

import javassist.build.IClassTransformer
import org.gradle.api.Project

/**
 * The default morpheus plugin. Creates an extension named 'morpheus'
 * represented by {@link MorpheusPluginExtension}.
 * The list of transformers are provided by:
 * <pre>
 *   morpheus {
 *     transformers = new T0(), new T1()
 *   }
 * </pre>
 */
class MorpheusPlugin extends AbstractMorpheusPlugin {

  @Override
  public IClassTransformer[] getTransformers(Project project) {
    return project.morpheus.transformers;
  }

  @Override
  protected Class getPluginExtension() {
    MorpheusPluginExtension
  }

  @Override
  protected String getExtension() {
    "morpheus"
  }
}
