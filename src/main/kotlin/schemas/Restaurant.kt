package schemas

import org.jetbrains.exposed.sql.Table

object Restaurant : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val name = varchar("name", length = 50)
  val picture = varchar("picture", length = 255)
  val description = varchar("description", length = 255)
  val address = varchar("address", length = 50)
  val website = varchar("website", length = 50)
  val phone = varchar("phone", length = 24)
}
