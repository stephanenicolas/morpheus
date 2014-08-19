package com.github.stephanenicolas.afterburner.android.sample;

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import com.darylteo.gradle.javassist.tasks.TransformationTask
import java.util.Set
import java.io.File

class MorpheusPlugin implements Plugin<Project> {
  @Override void apply(Project project) {
    def hasApp = project.plugins.withType(AppPlugin)
    def hasLib = project.plugins.withType(LibraryPlugin)
    if (!hasApp && !hasLib) {
      throw new IllegalStateException("'android' or 'android-library' plugin required.")
    }

    final def log = project.logger
    final def variants
    if (hasApp) {
      variants = project.android.applicationVariants
    } else {
      variants = project.android.libraryVariants
    }

    //project.dependencies {
    //}

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
      def transformTask = "transform${variant.name.capitalize()}"
      project.task(transformTask, type: TransformationTask) {
        description = 'Transform a file using the android example processor'
        into("${project.buildDir}/transformations/transform${variant.name.capitalize()}")
        from ("${javaCompile.destinationDir.path}")
        transformation = new com.github.stephanenicolas.afterburner.android.sample.ExampleProcessor()
        classpath = classpathSet
        outputs.upToDateWhen {
          false
        }

      }

      println "sourceFile: " + project.tasks.getByName(transformTask).getSource()
      project.tasks.getByName(transformTask).mustRunAfter(javaCompile)
      def copyTransformedTask = "copyTransformed${variant.name.capitalize()}"
      project.task(copyTransformedTask, type: Copy) {
        description = 'Copy transformed file to build dir'
        from ("${project.buildDir}/transformations/transform${variant.name.capitalize()}")
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
