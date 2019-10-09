# Scalafix rule for avoiding sensitive interpolations

[![Build Status](https://travis-ci.com/alejandrohdezma/sensitive-strings.svg?branch=master)](https://travis-ci.com/alejandrohdezma/sensitive-strings)

This rule reports errors when a "sensitive" type is used inside a string interpolation.

![](./images/lint-example.png)

## Installation

```sbt
scalafixDependencies += "com.alejandrohdezma" %% "sensitive-strings" % "0.1.0"
```

## Configuration

By default, this rule does not disable any particular type. Add them to `symbols` configuration.

```hocon
NoSensitiveStrings.symbols = []
```

### Examples

```hocon
NoSensitiveStrings.symbols = [
  com.alejandrohdezma.domain.Password,
  com.alejandrohdezma.domain.UserContext,
  com.alejandrohdezma.domain.UserAccount
]
```
