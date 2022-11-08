/*
 * Copyright 2019-2022 Alejandro Hernández <https://github.com/alejandrohdezma>
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

package com.alejandrohdezma.scalafix

object NoSensitiveStrings {

  /** Testing interpolating method parameters */
  // noinspection ScalaUnusedExpression
  def hello(boolean: Boolean, password: Password, context: Context, user: User): String = {
    s"$password"           // assert: NoSensitiveStrings
    s"$context"            // assert: NoSensitiveStrings
    s"${context.password}" // assert: NoSensitiveStrings
    s"$user"               // ok, no error messages
    s"$boolean"            // ok, no error messages
  }

  /** Testing interpolating class parameters */
  class ContextWrapped(val context: Context) {

    val string: String = s"$context" // assert: NoSensitiveStrings

  }

  /** Testing interpolating object values */
  val p: Password = Password("1234")

  val u: User = User("me")

  val c: Context = Context(u, p)

  val id: Password.Id = 5

  val wrap: ContextWrapped = new ContextWrapped(c)

  s"$c" // assert: NoSensitiveStrings

  s"$u" // ok, no error messages

  s"${c.password}" // assert: NoSensitiveStrings

  s"${wrap.context.password}" // assert: NoSensitiveStrings

  s"$id" // assert: NoSensitiveStrings

  s"$p" /* assert: NoSensitiveStrings
     ^
Don't use `p` inside string interpolations, it may contain sensitive information.
   */

}

final case class Password(value: String) extends AnyVal

object Password {

  type Id = Int

}

final case class User(value: String) extends AnyVal

final case class Context(user: User, password: Password)
