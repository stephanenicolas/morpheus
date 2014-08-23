Morpheus [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.stephanenicolas.morpheus/morpheus-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.stephanenicolas.morpheus/morpheus-plugin)
========

An Android gradle plugin to transform classes via javassist.

<img src="https://raw.githubusercontent.com/stephanenicolas/morpheus/master/assets/morpheus-logo.jpg"
width="250px" />

### Usage 

Morpheus can be used in 2 different ways : 
* to use a Javassist based class(es) transformer(s) via a gradle DSL
* to create a new gradle plugin to apply to an Android project.

### Use a Javassist based class(es) transformer(s)

Add morpheus to your `build.gradle` file:

```groovy
apply plugin: 'morpheus'
````

Then configure morpheus to use one or multiple [`ClassTransformer`](https://github.com/stephanenicolas/javassist-build-plugin-api/blob/master/src/main/java/javassist/build/IClassTransformer.java):

```groovy

android {
...
}

import foo.MyTransformer

morpheus {
  transformers = new MyTransformer()
}
```

Morpheus will then add a few tasks to your gradle android app (or lib) automatically :

```bash
gradle tasks --all

transformMyTransformerDebug
copyTransformedMyTransformerDebug
...
```

All variants of the build are enhanced to apply the transformations.

### Example

This repo contains an example to invoke a class transformer to an android projet.

### Create a new gradle plugin to apply to an Android project.

Here are some examples of android gradle plugins based on morpheus : 

* [LogLifeCycle](https://github.com/stephanenicolas/loglifecycle) to log the life cycle of annotated activities, fragments, etc..
* [InjectView](https://github.com/stephanenicolas/injectview) to automatically inject views and fragments into activities, fragments, views, place holder, etc. Works a la [RoboGuice](https://github.com/stephanenicolas/org.roboguice/roboguice).

### Related projects

It is related to :
* [javassist-build-plugin-api](https://github.com/stephanenicolas/javassist-build-plugin-api)
* [javassist-gradle-plugin](https://github.com/darylteo/javassist-gradle-plugin)

### Credits 

The logo of morpheus is a painting by [H Homampour](http://www.yessy.com/homampourarts/gallery.html?i=30994)

License
-------

	Copyright (C) 2014 Stéphane NICOLAS

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	     http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
