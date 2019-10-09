inThisBuild(
  List(
    scalaVersion := "2.12.10",
    organization := "com.alejandrohdezma",
    description  := "Scalafix rule for avoiding sensitive interpolations",
    homepage     := Some(url(alejandrohdezma.url + "/sensitive-strings")),
    licenses     := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    developers   := List(alejandrohdezma)
  )
)

skip in publish := true

lazy val rule = project
  .settings(moduleName := "sensitive-strings")
  .settings(libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0")
  .enablePlugins(ScalafixLintRule)

val alejandrohdezma = Developer(
  "alejandrohdezma",
  "Alejandro Hernández",
  "info@alejandrohdezma.com",
  url("https://github.com/alejandrohdezma")
)
