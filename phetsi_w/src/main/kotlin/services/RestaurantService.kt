package services

import databaseDriver
import databasePassword
import databaseUrl
import databaseUser
import models.Avis
import models.Restaurant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object RestaurantSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val name = varchar("password", length = 50)
}

object RestaurantAvisSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val userId = integer("user_id").uniqueIndex()
  val avisId = integer("avis_id").uniqueIndex()
  val restaurantId = integer("restaurant_id").uniqueIndex()
}

object AvisSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val score = integer("score")
  val content = varchar("content", length = 300)
}

fun findRestaurantById(id: Int): Restaurant? {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(RestaurantSchema)

    val findRestaurants = RestaurantSchema.select {
      RestaurantSchema.id eq id
    }

    if (findRestaurants.count() <= 0)
      return@transaction null

    val findRestaurant = findRestaurants.first()

    return@transaction Restaurant(findRestaurant[RestaurantSchema.name])
  }
}

fun findAllRestaurant(): Array<Restaurant> {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(RestaurantSchema)

    return@transaction RestaurantSchema
      .selectAll()
      .map {
        Restaurant(it[RestaurantSchema.name])
      }
      .toTypedArray()
  }
}

fun addRestaurant(restaurant: Restaurant): Boolean {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    RestaurantSchema.insert {
      it[name] = restaurant.name
    } get RestaurantSchema.id ?: return@transaction false

    return@transaction true
  }
}


fun addAvisToRestaurant(currentUserId: Int, currentRestaurantId: Int, avis: Avis): Boolean {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(RestaurantSchema, RestaurantAvisSchema, AvisSchema)

    val currentAvisId = AvisSchema.insert {
      it[score] = avis.score
      it[content] = avis.content
    } get AvisSchema.id ?: return@transaction false

    RestaurantAvisSchema.insert {
      it[userId] = currentUserId
      it[avisId] = currentAvisId
      it[restaurantId] = currentRestaurantId
    } get RestaurantAvisSchema.id ?: return@transaction false

    return@transaction true
  }
}
