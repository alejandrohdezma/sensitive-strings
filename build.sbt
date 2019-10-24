ThisBuild / scalaVersion := "2.12.10"
ThisBuild / repository   := "sensitive-strings"

lazy val `sensitive-strings` = project
  .in(file("."))
  .enablePlugins(ScalafixLintRule)
