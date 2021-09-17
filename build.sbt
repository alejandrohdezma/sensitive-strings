ThisBuild / scalaVersion := "2.12.15"
ThisBuild / organization := "com.alejandrohdezma"

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; mdoc; scalafixEnable; test")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll")
addCommandAlias("ci-publish", "github; ci-release")

lazy val scalafix = "ch.epfl.scala" %% "scalafix-core" % "[0.9.0,)" % Provided // scala-steward:off

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))

lazy val `sensitive-strings` = module
  .enablePlugins(ScalafixLintRule)
  .settings(libraryDependencies += scalafix)
