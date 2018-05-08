package schemas

import org.jetbrains.exposed.sql.Table

object Avis : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val score = integer("score")
  val content = varchar("content", length = 300)
}
