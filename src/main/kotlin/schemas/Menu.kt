package schemas

import org.jetbrains.exposed.sql.Table

object Menu : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val title = varchar("title", length = 20)
  val description = text("description")
  val price = varchar("price", length = 10)
}
