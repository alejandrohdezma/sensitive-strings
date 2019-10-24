import sbt.Keys._
import sbt._
import scalafix.sbt.BuildInfo.scalafixVersion
import scalafix.sbt.ScalafixTestkitPlugin
import scalafix.sbt.ScalafixTestkitPlugin.autoImport._

object ScalafixLintRule extends AutoPlugin {

  override def derivedProjects(proj: ProjectDefinition[_]) = {
    lazy val ref = LocalProject("tests")

    lazy val generateTests = Def.task {
      val file = (sourceManaged in Test).value / "fix" / "RuleSuite.scala"

      val suite = "class RuleSuite extends scalafix.testkit.SemanticRuleSuite() { runAllTests() }"

      IO.write(file, suite)

      Seq(file)
    }

    val tests = project
      .enablePlugins(ScalafixTestkitPlugin)
      .settings(skip in publish := true)
      .settings(scalafixTestkitOutputSourceDirectories := List.empty)
      .settings(scalafixTestkitInputClasspath := fullClasspath.in(ref, Compile).value)
      .settings(scalafixTestkitInputSourceDirectories := sourceDirectories.in(ref, Compile).value)
      .settings(libraryDependencies += testkit % Test cross CrossVersion.full)
      .settings(sourceGenerators in Test += generateTests.taskValue)
      .dependsOn(LocalProject(proj.id))

    Seq(tests)
  }

  override def projectSettings: Seq[Def.Setting[_]] = Seq(libraryDependencies += scalafix)

  val scalafix = "ch.epfl.scala" %% "scalafix-core" % scalafixVersion

  val testkit = "ch.epfl.scala" % "scalafix-testkit" % scalafixVersion

}
