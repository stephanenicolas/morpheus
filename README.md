Morpheus [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.stephanenicolas/morpheus/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.stephanenicolas/morpheus)
========

A gradle plugin to transform classes via javassist.

### Usage 

Morpheus can be used in 2 different ways : 
* to use a Javassist based class(es) transformer(s)
* to create a new gradle plugin to trigger it inside an Android project.


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

This repo contains an example. 

Also have a look at [LogLifeCycle](https://github.com/stephanenicolas/loglifecycle) to see how to extend morpheus.

### Related projects

It is related to :
* [javassist-build-plugin-api](https://github.com/stephanenicolas/javassist-build-plugin-api)
* [javassist-gradle-plugin](https://github.com/darylteo/javassist-gradle-plugin)

A good example of morpheus capabilities can be found here : 
* [LogLifeCycle](https://github.com/stephanenicolas/loglifecycle)

Stay tuned, this is just the beginning of larger story...

<!-- Give credit for logo to :
http://www.bigabstractpainting.com/ -->

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
