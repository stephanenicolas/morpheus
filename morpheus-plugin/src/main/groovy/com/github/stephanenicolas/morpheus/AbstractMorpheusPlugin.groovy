package com.github.stephanenicolas.morpheus

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.darylteo.gradle.javassist.tasks.TransformationTask
import javassist.build.IClassTransformer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile

public abstract class AbstractMorpheusPlugin implements Plugin<Project> {
  @Override
  public void apply(Project project) {
    def hasApp = project.plugins.withType(AppPlugin)
    def hasLib = project.plugins.withType(LibraryPlugin)
    if (!hasApp && !hasLib) {
      throw new IllegalStateException("'android' or 'android-library' plugin required.")
    }

    project.extensions.create(getExtension(), getPluginExtension())

    final def log = project.logger
    final String LOG_TAG = this.getClass().getName()

    final def variants
    if (hasApp) {
      variants = project.android.applicationVariants
    } else {
      variants = project.android.libraryVariants
    }

    configure(project)

    variants.all { variant ->
      log.debug(LOG_TAG, "Transforming classes in variant '${variant.name}'.")

      JavaCompile javaCompile = variant.javaCompile
      FileCollection classpathFileCollection = project.files(project.android.bootClasspath)
      classpathFileCollection += javaCompile.classpath

      for(IClassTransformer transformer : getTransformers(project) ) {
        String transformerClassName = transformer.getClass().getSimpleName()
        String transformationDir = "${project.buildDir}/intermediates/transformations/transform${transformerClassName}${variant.name.capitalize()}"

        def transformTask = "transform${transformerClassName}${variant.name.capitalize()}"
        project.task(transformTask, type: TransformationTask) {
          description = "Transform a file using ${transformerClassName}"
          into(transformationDir)
          from ("${javaCompile.destinationDir.path}")
          transformation = transformer
          classpath = classpathFileCollection
          outputs.upToDateWhen {
            false
          }
          eachFile {
            log.debug(LOG_TAG, "Transformed:" + it.path)
          }
        }

        project.tasks.getByName(transformTask).mustRunAfter(javaCompile)
        def copyTransformedTask = "copyTransformed${transformerClassName}${variant.name.capitalize()}"
        project.task(copyTransformedTask, type: Copy) {
          description = "Copy transformed file to build dir for ${transformerClassName}"
          from (transformationDir)
          into ("${javaCompile.destinationDir.path}")
          outputs.upToDateWhen {
            false
          }
          eachFile {
            log.debug(LOG_TAG, "Copied into build:" + it.path) 
          }
        }
        project.tasks.getByName(copyTransformedTask).mustRunAfter(project.tasks.getByName(transformTask))
        Task assemble = variant.assemble
        assemble.dependsOn(transformTask, copyTransformedTask)
        Task install = variant.install
        install.dependsOn(transformTask, copyTransformedTask)
      }
    }
  }

  protected void configure(Project project) {
  }

  protected abstract Class getPluginExtension()

  protected abstract String getExtension()

  public abstract IClassTransformer[] getTransformers(Project project);
}
