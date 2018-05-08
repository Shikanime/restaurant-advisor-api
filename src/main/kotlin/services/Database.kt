package services

import databaseDriver
import databasePassword
import databaseURI
import databaseUser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import schemas.*

fun <T> runQuery(callback: Transaction.() -> T): T {
  Database.connect(databaseURI, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(
      User,
      Restaurant,
      RestaurantUser)

    create(Avis,
      RestaurantAvis)

    create(Menu,
      RestaurantMenu)

    return@transaction callback()
  }
}
