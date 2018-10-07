[![Travis](https://img.shields.io/travis/ccguyka/show-updates-maven-plugin.svg?style=flat-square)](https://travis-ci.org/ccguyka/show-updates-maven-plugin)
[![Coverage](https://img.shields.io/coveralls/github/ccguyka/show-updates-maven-plugin.svg?style=flat-square)](https://coveralls.io/github/ccguyka/show-updates-maven-plugin)
[![Dependency Status](https://www.versioneye.com/user/projects/5af45dec0fb24f0e5d5149d7/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5af45dec0fb24f0e5d5149d7)
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

| name     | default    | description                                                             |
|----------|------------|-------------------------------------------------------------------------|
| excludes | alpha,beta | Dependency key words to be excluded from result as comma separated list |
