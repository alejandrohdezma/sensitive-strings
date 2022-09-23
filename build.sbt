ThisBuild / scalaVersion   := "2.13.9"
ThisBuild / organization   := "com.alejandrohdezma"
ThisBuild / publish / skip := true

addCommandAlias("ci-test", "fix --check; mdoc; test")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll")
addCommandAlias("ci-publish", "github; ci-release")

lazy val V = _root_.scalafix.sbt.BuildInfo

lazy val rulesCrossVersions = Seq(V.scala213, V.scala212)

lazy val `sensitive-strings` = projectMatrix
  .settings(publish / skip := false)
  .settings(libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion)
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val input = projectMatrix
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions)

lazy val testsAggregate = Project("tests", file("target/testsAggregate"))
  .aggregate(tests.projectRefs: _*)

lazy val tests = projectMatrix
  .dependsOn(`sensitive-strings`)
  .settings(
    libraryDependencies                    += ("ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test).cross(CrossVersion.full),
    libraryDependencies                   ++= scalafixDependencies.value.map(_ % Test),
    scalafixTestkitOutputSourceDirectories := Nil,
    scalafixTestkitInputSourceDirectories  := TargetAxis.resolve(input, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath          := TargetAxis.resolve(input, Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions      := TargetAxis.resolve(input, Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion       := TargetAxis.resolve(input, Compile / scalaVersion).value
  )
  .defaultAxes(rulesCrossVersions.map(VirtualAxis.scalaABIVersion) :+ VirtualAxis.jvm: _*)
  .customRow(List(V.scala213), List(TargetAxis(V.scala213), VirtualAxis.jvm), Nil)
  .customRow(List(V.scala212), List(TargetAxis(V.scala212), VirtualAxis.jvm), Nil)
  .enablePlugins(ScalafixTestkitPlugin)

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))
