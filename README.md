# `@MaybBeSlow` Annotation for JUnit5 Tests

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/yegor256/maybeslow)](http://www.rultor.com/p/yegor256/maybeslow)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/yegor256/maybeslow/actions/workflows/mvn.yml/badge.svg)](https://github.com/yegor256/maybeslow/actions/workflows/mvn.yml)
[![PDD status](http://www.0pdd.com/svg?name=yegor256/maybeslow)](http://www.0pdd.com/p?name=yegor256/maybeslow)
[![Maven Central](https://img.shields.io/maven-central/v/com.yegor256/maybeslow.svg)](https://maven-badges.herokuapp.com/maven-central/com.yegor256/maybeslow)
[![Javadoc](http://www.javadoc.io/badge/com.yegor256/maybeslow.svg)](http://www.javadoc.io/doc/com.yegor256/maybeslow)
[![codecov](https://codecov.io/gh/yegor256/maybeslow/branch/master/graph/badge.svg)](https://codecov.io/gh/yegor256/maybeslow)
[![Hits-of-Code](https://hitsofcode.com/github/yegor256/maybeslow)](https://hitsofcode.com/view/github/yegor256/maybeslow)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/yegor256/maybeslow/blob/master/LICENSE.txt)

Sometimes, a test may be slow in. If such a test doesn't write anything
to the console, it may be hard to visually understand whether the test is
still alive and running or it's already hung up. The annotation provided
by this package helps you make your JUnit5 tests visually more responsive:
if they take longer than just _a second_ to run, the console will 
start getting regular notification messages, informing you that the 
test is still alive and running.

First, you add this to your `pom.xml`:

```xml
<dependency>
  <groupId>com.yegor256</groupId>
  <artifactId>maybeslow</artifactId>
  <version>0.0.1</version>
</dependency>
```

Then, you use it like this, in your [JUnit5][junit] test:

```java

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

class FooTest {
    @Test
    @ExtendWith(MayBeSlow.class)
    void slowTest() {
        // If this test takes longer than a second,
        // there will be notifications in the console
    }
}
```

## How to Contribute

Fork repository, make changes, send us a
[pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full Maven build:

```bash
mvn clean install -Pqulice
```

You will need Maven 3.3+ and Java 11+.

[junit]: https://junit.org/junit5/
