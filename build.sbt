ThisBuild / scalaVersion := "2.12.10"

lazy val `sensitive-strings` = project
  .in(file("."))
  .enablePlugins(ScalafixLintRule, MdocPlugin)
  .settings(mdocOut := file("."))
