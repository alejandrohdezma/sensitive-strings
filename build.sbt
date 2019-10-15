lazy val `sensitive-strings` = project
  .in(file("."))
  .enablePlugins(ScalafixLintRule)
  .settings(
    scalaVersion        := "2.12.10",
    organization        := "com.alejandrohdezma",
    description         := "Scalafix rule for avoiding sensitive interpolations",
    homepage            := Some(url(alejandrohdezma.url + "/sensitive-strings")),
    licenses            := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    developers          := List(alejandrohdezma),
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"
  )

val alejandrohdezma = Developer(
  "alejandrohdezma",
  "Alejandro Hernández",
  "info@alejandrohdezma.com",
  url("https://github.com/alejandrohdezma")
)
