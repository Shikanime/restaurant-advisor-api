package schemas

import org.jetbrains.exposed.sql.Table

object User : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val firstName = varchar("firstName", length = 20)
  val lastName = varchar("lastName", length = 20)
  val email = varchar("email", length = 50)
  val password = varchar("password", length = 60)
  val birthday = date("birthday")
}
