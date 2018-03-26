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
  val userId = integer("user_id")
  val avisId = integer("avis_id")
  val restaurantId = integer("restaurant_id")
}

object AvisSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val score = integer("score")
  val content = varchar("content", length = 300)
}

fun findRestaurantById(id: Int): Restaurant {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(RestaurantSchema)

    val findRestaurants = RestaurantSchema.select {
      RestaurantSchema.id eq id
    }

    if (findRestaurants.count() <= 0)
      throw Exception("Cannot find restaurant")

    return@transaction Restaurant(findRestaurants.first()[RestaurantSchema.name])
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

fun addRestaurant(restaurant: Restaurant): Int {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    return@transaction RestaurantSchema.insert {
      it[name] = restaurant.name
    } get RestaurantSchema.id
      ?: throw Exception("Fail to create new restaurant retry again later")
  }
}


fun addAvisToRestaurant(currentUserId: Int, currentRestaurantId: Int, avis: Avis): Int {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(RestaurantSchema, RestaurantAvisSchema, AvisSchema)

    if (avis.score > 5 || avis.score < 0)
      throw Exception("Score ${avis.score} must be between 0 or 5")

    if (findRestaurantById(currentRestaurantId) == null)
      throw Exception("$currentRestaurantId id doesn't exist")

    if (findUserById(currentUserId) == null)
      throw Exception("$currentUserId doesn't exist")

    val currentAvisId = AvisSchema.insert {
      it[score] = avis.score
      it[content] = avis.content
    } get AvisSchema.id
      ?: throw Exception("Avis creation failed, retry again")

    return@transaction RestaurantAvisSchema.insert {
      it[userId] = currentUserId
      it[avisId] = currentAvisId
      it[restaurantId] = currentRestaurantId
    } get RestaurantAvisSchema.id
      ?: throw Exception("Restaurant avis creation failed, retry again")
  }
}
