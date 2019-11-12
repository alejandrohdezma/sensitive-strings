# Scalafix rule for avoiding sensitive interpolations

[![][travis-badge]][travis] [![][maven-badge]][maven] [![][steward-badge]][steward] [![][mergify-badge]][mergify]

This rule reports errors when a "sensitive" type is used inside a string interpolation.

![](./images/lint-example.png)

## Installation

```sbt
scalafixDependencies += "com.alejandrohdezma" %% "sensitive-strings" % "@VERSION@"
```

## Configuration

By default, this rule does not disable any particular type. Add them to `symbols` configuration.

```hocon
NoSensitiveStrings.symbols = []
NoSensitiveStrings.regex = []
```

### Examples

```hocon
NoSensitiveStrings.symbols = [
  com.alejandrohdezma.domain.Password,
  com.alejandrohdezma.domain.UserContext,
  com.alejandrohdezma.domain.UserAccount
]
NoSensitiveStrings.regex = [
  "com\\.alejandrohdezma\\.domain\\..*"
]
```

#### Regex

As you can see in the previous example, you can also match against a list of regex using the `regex` configuration and providing a list of patterns.

[travis]: https://travis-ci.com/alejandrohdezma/sensitive-strings
[travis-badge]: https://travis-ci.com/alejandrohdezma/sensitive-strings.svg?branch=master

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sensitive-strings_2.12
[maven-badge]: https://img.shields.io/maven-central/v/com.alejandrohdezma/sensitive-strings_2.12?color=green

[mergify]: https://mergify.io
[mergify-badge]: https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/alejandrohdezma/sensitive-strings&style=flat

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=