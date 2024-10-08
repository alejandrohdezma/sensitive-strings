ThisBuild / scalaVersion           := "2.13.14"
ThisBuild / organization           := "com.alejandrohdezma"
ThisBuild / publish / skip         := true
ThisBuild / versionPolicyIntention := Compatibility.BinaryAndSourceCompatible

addCommandAlias("ci-test", "fix --check; versionPolicyCheck; mdoc; test")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll")
addCommandAlias("ci-publish", "versionCheck; github; ci-release")

lazy val V = _root_.scalafix.sbt.BuildInfo

lazy val rulesCrossVersions = Seq(V.scala213, V.scala212)

lazy val `sensitive-strings` = projectMatrix
  .settings(publish / skip := false)
  .settings(libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion)
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val input = projectMatrix
  .settings(headerSources / excludeFilter := HiddenFileFilter || "*NoSensitiveStrings.scala")
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions)
  .settings(scalacOptions -= "-Wnonunit-statement")

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
  .settings(scalacOptions -= "-Wnonunit-statement")

lazy val documentation = project
  .enablePlugins(MdocPlugin)
