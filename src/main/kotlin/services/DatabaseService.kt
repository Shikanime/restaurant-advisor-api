package services

import databaseDriver
import databasePassword
import databaseUrl
import databaseUser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

object UserSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val firstName = varchar("firstName", length = 20)
  val lastName = varchar("lastName", length = 20)
  val email = varchar("email", length = 50)
  val password = varchar("password", length = 60)
}

object RestaurantSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val name = varchar("password", length = 50)
}

object RestaurantAvisSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val userId = integer("user_id")
  val avisId = integer("avis_id")
  val restaurantId = integer("restaurant_id")
}

object AvisSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val score = integer("score")
  val content = varchar("content", length = 300)
}

fun <T> runQuery(callback: Transaction.() -> T): T {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(
      UserSchema,
      RestaurantSchema,
      AvisSchema,
      RestaurantAvisSchema)

    return@transaction callback()
  }
}
