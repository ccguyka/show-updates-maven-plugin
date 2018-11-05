[![Travis](https://img.shields.io/travis/ccguyka/show-updates-maven-plugin.svg?style=flat-square)](https://travis-ci.org/ccguyka/show-updates-maven-plugin)
[![Coverage](https://img.shields.io/coveralls/github/ccguyka/show-updates-maven-plugin.svg?style=flat-square)](https://coveralls.io/github/ccguyka/show-updates-maven-plugin)
[![Known Vulnerabilities](https://snyk.io/test/github/ccguyka/show-updates-maven-plugin/badge.svg?style=flat-square)](https://snyk.io/test/github/ccguyka/show-updates-maven-plugin)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)

# Show Maven Updates Plugin

Show the latest update version of maven dependencies.

# Usage

## How to build it

```
mvn install
```

## How to use it

```
mvn com.github.ccguyka:show-updates-maven-plugin:updates
```

## Properties

| name     | default             | description                                                             |
|----------|---------------------|-------------------------------------------------------------------------|
| excludes | alpha,beta,SNAPSHOT | Dependency key words to be excluded from result as comma separated list |
| versions | major               | Show either major or minor version updates.                             |

## Example

This is how the output will look like. In this case the `guava-testlib` module of [guava](https://github.com/google/guava/tree/v27.0) v27.0


```
[INFO] --------------------< com.google.guava:guava-tests >--------------------
[INFO] Building Guava Unit Tests 27.0-jre                                 [4/5]
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- show-updates-maven-plugin:0.0.1-SNAPSHOT:updates (default-cli) @ guava-tests ---
[INFO] artifact com.google.guava:guava-testlib: checking for updates from central
[INFO] Available dependency updates:
[INFO]   org.checkerframework:checker-qual ... 2.5.2 -> 2.5.7
[INFO]   com.google.errorprone:error_prone_annotations ... 2.2.0 -> 2.3.2
[INFO]   org.easymock:easymock ... 3.0 -> 4.0.1
[INFO]   org.mockito:mockito-core ... 2.19.0 -> 2.23.0
[INFO] Available plugin updates:
[INFO]   org.apache.maven.plugins:maven-jar-plugin ... 3.0.2 -> 3.1.0
[INFO]   org.apache.maven.plugins:maven-source-plugin ... 2.1.2 -> 3.0.1
[INFO]   org.apache.maven.plugins:maven-surefire-plugin ... 2.7.2 -> 2.22.1
[INFO]   org.codehaus.mojo:build-helper-maven-plugin ... 1.7 -> 3.0.0
[INFO]   org.apache.maven.plugins:maven-compiler-plugin ... 3.6.1 -> 3.8.0
[INFO]   org.apache.maven.plugins:maven-deploy-plugin ... 2.8.2 -> 3.0.0-M1
[INFO] Available dependency management updates:
[INFO]   org.checkerframework:checker-qual ... 2.5.2 -> 2.5.7
[INFO]   com.google.errorprone:error_prone_annotations ... 2.2.0 -> 2.3.2
[INFO]   org.easymock:easymock ... 3.0 -> 4.0.1
[INFO]   org.mockito:mockito-core ... 2.19.0 -> 2.23.0
```
