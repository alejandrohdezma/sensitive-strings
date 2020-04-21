ThisBuild / scalaVersion := "2.12.11"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc; test")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")

lazy val scalafix = "ch.epfl.scala" %% "scalafix-core" % "[0.9.0,)" % Provided // scala-steward:off

skip in publish := true

lazy val docs = project
  .in(file("sensitive-strings-docs"))
  .enablePlugins(MdocPlugin)
  .settings(skip in publish := true)
  .settings(mdocOut := file("."))

lazy val `sensitive-strings` = project
  .enablePlugins(ScalafixLintRule)
  .settings(libraryDependencies += scalafix)
