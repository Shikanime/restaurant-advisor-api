package services

import models.Avis
import models.Restaurant
import org.jetbrains.exposed.sql.*

fun findRestaurantById(id: Int): Restaurant {
  return runQuery {
    val foundRestaurants = RestaurantSchema.select {
      RestaurantSchema.id eq id
    }

    if (foundRestaurants.count() <= 0)
      throw Exception("Cannot find restaurant")

    val foundRestaurant = foundRestaurants.first()

    return@runQuery Restaurant(
      foundRestaurant [RestaurantSchema.name],
      foundRestaurant [RestaurantSchema.address],
      foundRestaurant [RestaurantSchema.website],
      foundRestaurant [RestaurantSchema.phone])
  }
}

fun findRestaurantByName(name: String): Restaurant {
  return runQuery {
    val foundRestaurants = RestaurantSchema.select {
      RestaurantSchema.name eq name
    }

    if (foundRestaurants.count() <= 0)
      throw Exception("Cannot find restaurant")

    val foundRestaurant = foundRestaurants.first()

    return@runQuery Restaurant(
      foundRestaurant [RestaurantSchema.name],
      foundRestaurant [RestaurantSchema.address],
      foundRestaurant [RestaurantSchema.website],
      foundRestaurant [RestaurantSchema.phone])
  }
}

fun findAllRestaurant(): Array<Restaurant> {
  return runQuery {
    return@runQuery RestaurantSchema
      .selectAll()
      .map {
        Restaurant(
          it[RestaurantSchema.name],
          it[RestaurantSchema.address],
          it[RestaurantSchema.website],
          it[RestaurantSchema.phone])
      }
      .toTypedArray()
  }
}

fun addRestaurant(restaurant: Restaurant): Int {
  return runQuery {
    when {
      restaurant.name == null ->
        throw Exception("Empty name")
      restaurant.address == null ->
        throw Exception("Empty address")
      restaurant.website == null ->
        throw Exception("Empty website ")
      restaurant.phone == null ->
        throw Exception("Empty phone")

      restaurant.name.length > 50 ->
        throw Exception("Invalid name length, max is 50 characters")
      restaurant.address.length > 50 ->
        throw Exception("Invalid address length, max is 20 characters")
      restaurant.website.length > 50 ->
        throw Exception("Invalid website length, max is 50 characters")
      restaurant.phone.length > 24 ->
        throw Exception("Invalid phone length, max is 24 characters")
    }

    return@runQuery RestaurantSchema.insert {
      it[name] = restaurant.name ?: ""
      it[address] = restaurant.address ?: ""
      it[website] = restaurant.website ?: ""
      it[phone] = restaurant.phone ?: ""
    } get RestaurantSchema.id
      ?: throw Exception("Fail to create new restaurant retry again later")
  }
}

fun updateRestaurantById(id: Int, restaurant: Restaurant): Int {
  return runQuery {
    val foundRestaurants = RestaurantSchema.select {
      RestaurantSchema.id.eq(id)
    }

    if (foundRestaurants.count() <= 0)
      throw Exception("Cannot find user from email")

    val foundRestaurant= foundRestaurants.first()

    return@runQuery RestaurantSchema.update({
      RestaurantSchema.id.eq(id)
    }){
      it[name] = restaurant.name ?: foundRestaurant[RestaurantSchema.name]
      it[address] = restaurant.address ?: foundRestaurant[RestaurantSchema.address]
      it[website] = restaurant.website ?: foundRestaurant[RestaurantSchema.website]
      it[phone] = restaurant.phone ?: foundRestaurant[RestaurantSchema.phone]
    }
  }
}

fun addAvisToRestaurant(currentUserId: Int, currentRestaurantId: Int, avis: Avis): Int {
  return runQuery {
    if (avis.score > 5 || avis.score < 0)
      throw Exception("Score ${avis.score} must be between 0 or 5")

    findRestaurantById(currentRestaurantId)
    findUserById(currentUserId)

    val currentAvisId = AvisSchema.insert {
      it[score] = avis.score
      it[content] = avis.content
    } get AvisSchema.id
      ?: throw Exception("Avis creation failed, retry again")

    return@runQuery RestaurantAvisSchema.insert {
      it[userId] = currentUserId
      it[avisId] = currentAvisId
      it[restaurantId] = currentRestaurantId
    } get RestaurantAvisSchema.id
      ?: throw Exception("Restaurant avis creation failed, retry again")
  }
}

fun deleteAvisByRestaurantId(currentUserId: Int, currentRestaurantId: Int): Int {
  return runQuery {
    val foundRestaurants = RestaurantAvisSchema.select {
      RestaurantAvisSchema.restaurantId.eq(currentRestaurantId)
    }

    if (foundRestaurants.count() <= 0)
      throw Exception("Cannot find restaurant")

    RestaurantAvisSchema.deleteWhere {
      RestaurantAvisSchema.restaurantId.eq(currentRestaurantId)
      RestaurantAvisSchema.userId.eq(currentUserId)
    }

    return@runQuery AvisSchema.deleteWhere {
      AvisSchema.id.eq(foundRestaurants.first()[RestaurantAvisSchema.avisId])
    }
  }
}

fun deleteAvisByAvisId(currentUserId: Int, currentAvisId: Int): Int {
  return runQuery {
    val foundRestaurants = RestaurantAvisSchema.select {
      RestaurantAvisSchema.avisId.eq(currentAvisId)
    }

    if (foundRestaurants.count() <= 0)
      throw Exception("Cannot find restaurant")

    RestaurantAvisSchema.deleteWhere {
      RestaurantAvisSchema.avisId.eq(currentAvisId)
      RestaurantAvisSchema.userId.eq(currentUserId)
    }

    return@runQuery AvisSchema.deleteWhere {
      AvisSchema.id.eq(foundRestaurants.first()[RestaurantAvisSchema.avisId])
    }
  }
}
