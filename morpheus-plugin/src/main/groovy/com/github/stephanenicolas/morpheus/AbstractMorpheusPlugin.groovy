package com.github.stephanenicolas.morpheus

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.darylteo.gradle.javassist.tasks.TransformationTask
import javassist.build.IClassTransformer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
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
    final def variants
    if (hasApp) {
      variants = project.android.applicationVariants
    } else {
      variants = project.android.libraryVariants
    }

    //project.dependencies {
    //}
    configureProject(project)

    variants.all { variant ->
      if (!variant.buildType.isDebuggable()) {
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
        return;
      }

      JavaCompile javaCompile = variant.javaCompile
      println project.android.bootClasspath.get(0).getClass().getName()
      Set<File> classpathSet = new HashSet<>();
      classpathSet.addAll(javaCompile.classpath.getFiles());
      for (String fileName : project.android.bootClasspath) {
        classpathSet.add(new File(fileName))
      }

      for(IClassTransformer transformer : getTransformers(project) ) {
        String transformerClassName = transformer.getClass().getSimpleName()
        String transformationDir = "${project.buildDir}/transformations/transform${transformerClassName}${variant.name.capitalize()}"
        def transformTask = "transform${transformerClassName}${variant.name.capitalize()}"
        project.task(transformTask, type: TransformationTask) {
          description = "Transform a file using ${transformerClassName}"
          into(transformationDir)
          from ("${javaCompile.destinationDir.path}")
          transformation = transformer
          classpath = classpathSet
          outputs.upToDateWhen {
            false
          }

        }

        println "sourceFile: " + project.tasks.getByName(transformTask).getSource()
        project.tasks.getByName(transformTask).mustRunAfter(javaCompile)
        def copyTransformedTask = "copyTransformed${transformerClassName}${variant.name.capitalize()}"
        project.task(copyTransformedTask, type: Copy) {
          description = "Copy transformed file to build dir for ${transformerClassName}"
          from (transformationDir)
          into ("${javaCompile.destinationDir.path}")
          outputs.upToDateWhen {
            false
          }
          eachFile { println "Copied:" + it.path }
        }
        project.tasks.getByName(copyTransformedTask).mustRunAfter(project.tasks.getByName(transformTask))
        Task assemble = variant.assemble
        assemble.dependsOn(transformTask, copyTransformedTask)
        Task install = variant.install
        install.dependsOn(transformTask, copyTransformedTask)
      }
    }
  }

  protected void configureProject(Project project) {
  }

  protected abstract Class getPluginExtension()

  protected abstract String getExtension()

  public abstract IClassTransformer[] getTransformers(Project project);
}
