/*
rule = NoSensitiveStrings
NoSensitiveStrings.symbols = [
  com.alejandrohdezma.scalafix.Context
]
NoSensitiveStrings.regex = [
  "com\\.alejandrohdezma\\.scalafix\\.Password.*"
]
 */

package com.alejandrohdezma.scalafix

object NoSensitiveStrings {

  /** Testing interpolating method parameters */
  //noinspection ScalaUnusedExpression
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
  val p: Password          = Password("1234")
  val u: User              = User("me")
  val c: Context           = Context(u, p)
  val id: Password.Id      = 5
  val wrap: ContextWrapped = new ContextWrapped(c)

  s"$c"                       // assert: NoSensitiveStrings
  s"$u"                       // ok, no error messages
  s"${c.password}"            // assert: NoSensitiveStrings
  s"${wrap.context.password}" // assert: NoSensitiveStrings
  s"$id"                      // assert: NoSensitiveStrings

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
