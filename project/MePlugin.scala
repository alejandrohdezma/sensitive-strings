import cats.implicits._
import io.circe.generic.auto._
import io.circe.parser._
import sbt.Def.Setting
import sbt.Keys._
import sbt.{url, AutoPlugin, Developer}

import scala.io.Source.fromURL
import scala.util.Try

object MePlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = {
    val alejandrohdezma = Developer(
      "alejandrohdezma",
      "Alejandro Hernández",
      "info@alejandrohdezma.com",
      url("https://github.com/alejandrohdezma")
    )

    def retrieveRepository(name: String) = {
      val url = s"https://api.github.com/repos/alejandrohdezma/$name"

      for {
        stage <- sys.env.get("TRAVIS_BUILD_STAGE_NAME")
        if stage === "Release"
        data       <- Try(fromURL(url)).toOption
        repository <- decode[Repository](data.mkString).toOption
      } yield repository
    }

    val githubDescription = retrieveRepository(_: String).map(_.description)

    val githubLicense = retrieveRepository(_: String).map { repository =>
      List(repository.license.spdx_id -> url(repository.license.url))
    }

    Seq(
      description  := githubDescription(moduleName.value).orEmpty,
      organization := "com.alejandrohdezma",
      homepage     := Some(url(s"${alejandrohdezma.url}/${moduleName.value}")),
      licenses     := githubLicense(moduleName.value).getOrElse(defaultLicense),
      developers   := List(alejandrohdezma)
    )
  }

  private val defaultLicense = List("MIT" -> url("http://opensource.org/licenses/MIT"))

  final private case class Repository(description: String, license: License)

  final private case class License(spdx_id: String, url: String)

}
