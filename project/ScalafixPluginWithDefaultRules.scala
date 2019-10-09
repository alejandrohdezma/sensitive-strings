import sbt.Keys.scalacOptions
import sbt._
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._

object ScalafixPluginWithDefaultRules extends AutoPlugin {

  private val scalafixRules: Seq[ModuleID] = Seq(
    "com.github.vovapolu" %% "scaluzzi"         % "0.1.3",
    "com.nequissimus"     %% "sort-imports"     % "0.3.0",
    "com.eed3si9n.fix"    %% "scalafix-noinfer" % "0.1.0-M1"
  )

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin

  override def projectSettings: Seq[Setting[_]] = Seq(
    scalacOptions += "-P:semanticdb:synthetics:on",
    scalafixDependencies in ThisBuild ++= scalafixRules,
    addCompilerPlugin(scalafixSemanticdb)
  )

}
