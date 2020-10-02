/*
 * Copyright 2019-2020 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.scalafix.rules

import java.util.regex.Pattern

import scala.Function.unlift
import scala.meta.Term.Interpolate
import scala.meta.Term.Name
import scala.meta.Term.Select
import scala.meta._

import com.alejandrohdezma.scalafix.rules.NoSensitiveStrings.Config
import metaconfig.ConfDecoder
import metaconfig.Configured
import metaconfig.generic.Surface
import scalafix.internal.config.ScalafixMetaconfigReaders.PatternDecoder
import scalafix.lint.Diagnostic
import scalafix.v1._

final case class NoSensitiveStrings(config: Config) extends SemanticRule("NoSensitiveStrings") {

  import config._

  def this() = this(Config.default)

  override def isLinter: Boolean = true

  override def description: String =
    "Triggers an error when a sensitive type variable is found within an interpolation." +
      " Sensitive types can be defined using the `sensitiveTypes` (array) configuration."

  override def fix(implicit doc: SemanticDocument): Patch =
    doc.tree.collect { case Interpolate(Name("s"), _, args) =>
      args collect unlift {
        case name @ Name(_)  => checkSensitiveString(name)
        case Select(_, name) => checkSensitiveString(name)
        case _               => None
      }
    }.flatten.asPatch

  /** Checks if the provided name is a sensible type */
  def checkSensitiveString(name: Name)(implicit D: SemanticDocument): Option[Patch] =
    name.symbol.info.map(_.signature).collect {
      case ValueSignature(TypeRef(_, Sensitive(_), _))        => sensitiveString(name)
      case MethodSignature(_, _, TypeRef(_, Sensitive(_), _)) => sensitiveString(name)
    }

  /** Creates a sensitive string lint patch for the provided term */
  def sensitiveString(name: Name): Patch = {
    val message = s"Don't use `$name` inside string interpolations, " +
      "it may contain sensitive information."

    val diagnostic = Diagnostic("", message, name.pos, "")

    Patch.lint(diagnostic)
  }

  override def withConfiguration(config: Configuration): Configured[Rule] =
    config.conf.getOrElse("NoSensitiveStrings")(this.config).map(NoSensitiveStrings(_))

}

object NoSensitiveStrings {

  /** Configuration for the rule, contains a list of the sensitive symbols */
  final case class Config(symbols: List[String], regex: List[Pattern]) {

    private def matchesRegex(string: String): Boolean =
      regex.exists(p => string.matches(p.pattern()))

    private val regexMatcher: SymbolMatcher = symbol => matchesRegex(symbol.normalized.value)

    /** Checks if a symbol is marked as sensitive */
    val Sensitive: SymbolMatcher = SymbolMatcher.normalized(symbols: _*) + regexMatcher

  }

  object Config {

    def default: Config = Config(Nil, Nil)

    implicit val surface: Surface[Config] =
      metaconfig.generic.deriveSurface[Config]

    implicit val decoder: ConfDecoder[Config] =
      metaconfig.generic.deriveDecoder(default)

  }

}
