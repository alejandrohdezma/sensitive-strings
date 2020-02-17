ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; test")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")

lazy val `sensitive-strings` = project
  .in(file("."))
  .enablePlugins(ScalafixLintRule, MdocPlugin)
  .settings(mdocOut := file("."))
