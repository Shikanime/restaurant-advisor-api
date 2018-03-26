package models

import org.joda.time.DateTime

data class User(
  val firstName: String,
  val lastName: String,
  val email: String,
  val password: String,
  val birthday: String
)
