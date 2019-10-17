Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.12.10"

enablePlugins(ScalafixLintRule)

lazy val `sensitive-strings` = project.in(file("."))
