ThisBuild / scalaVersion := "2.12.11"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; test")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")

skip in publish := true

lazy val docs = project
  .in(file("sensitive-strings-docs"))
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))

lazy val `sensitive-strings` = project.enablePlugins(ScalafixLintRule)
