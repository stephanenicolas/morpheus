package com.github.stephanenicolas.morpheus;

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import javassist.build.IClassTransformer
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import com.darylteo.gradle.javassist.tasks.TransformationTask
import java.util.Set
import java.io.File

class MorpheusPlugin extends AbstractMorpheusPlugin {

  public IClassTransformer[] getTransformers(Project project) {
    return project.morpheus.transformers;
  }

  protected Class getPluginExtension() {
    MorpheusPluginExtension
  }

  protected String getExtension() {
    "morpheus"
  }
}
