[![Travis](https://img.shields.io/travis/ccguyka/show-updates-maven-plugin.svg?style=flat-square)](https://travis-ci.org/ccguyka/show-updates-maven-plugin)
[![Dependency Status](https://www.versioneye.com/user/projects/5af45dec0fb24f0e5d5149d7/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5af45dec0fb24f0e5d5149d7)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)

# Show Maven Updates Plugin

Show the latest update version of maven dependencies.

# Usage

How to build it

```
mvn install
```

Also build help mojo

```
mvn install org.apache.maven.plugins:maven-plugin-plugin:helpmojo
```

How to use it

```
mvn com.github.ccguyka:show-updates-maven-plugin:updates
```

Show help

```
mvn com.github.ccguyka:show-updates-maven-plugin:help
```

which will print

```
This plugin has 2 goals:

show-updates:help
  Display help information on show-updates-maven-plugin.
  Call mvn show-updates:help -Ddetail=true -Dgoal=<goal-name> to display
  parameter details.

show-updates:updates
  Shows all dependencies and parent updates.
```
