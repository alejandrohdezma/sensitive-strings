# Scalafix rule for avoiding sensitive interpolations

[![Build Status](https://travis-ci.com/alejandrohdezma/sensitive-strings.svg?branch=master)](https://travis-ci.com/alejandrohdezma/sensitive-strings) ![Maven Central](https://img.shields.io/maven-central/v/com.alejandrohdezma/sensitive-strings_2.12?color=green) [![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

This rule reports errors when a "sensitive" type is used inside a string interpolation.

![](./images/lint-example.png)

## Installation

```sbt
scalafixDependencies += "com.alejandrohdezma" %% "sensitive-strings" % "0.1.1"
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
