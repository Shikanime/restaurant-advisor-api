package services

import models.Restaurant
import models.ServiceError
import org.jetbrains.exposed.sql.*

fun getRestaurant(): Array<Restaurant> {
  return runQuery {
    return@runQuery schemas.Restaurant
      .selectAll()
      .map {
        Restaurant(
          it[schemas.Restaurant.name],
          it[schemas.Restaurant.address],
          it[schemas.Restaurant.picture],
          it[schemas.Restaurant.description],
          it[schemas.Restaurant.website],
          it[schemas.Restaurant.phone])
      }
      .toTypedArray()
  }
}

fun addRestaurant(currentUserId: Int, restaurant: Restaurant): Int {
  return runQuery {
    when {
      restaurant.name == null ->
        throw ServiceError("Empty name")
      restaurant.address == null ->
        throw ServiceError("Empty address")
      restaurant.picture == null ->
        throw ServiceError("Empty picture")
      restaurant.description == null ->
        throw ServiceError("Empty desciption")
      restaurant.website == null ->
        throw ServiceError("Empty website ")
      restaurant.phone == null ->
        throw ServiceError("Empty phone")

      restaurant.name.length > 50 ->
        throw ServiceError("Invalid name length, max is 50 characters")
      restaurant.picture.length > 255 ->
        throw ServiceError("Invalid picture length, max is 255 characters")
      restaurant.description.length > 255 ->
        throw ServiceError("Invalid desciption length, max is 255 characters")
      restaurant.address.length > 50 ->
        throw ServiceError("Invalid address length, max is 20 characters")
      restaurant.website.length > 50 ->
        throw ServiceError("Invalid website length, max is 50 characters")
      restaurant.phone.length > 24 ->
        throw ServiceError("Invalid phone length, max is 24 characters")
    }

    findUserById(currentUserId)

    val currentRestaurantId = schemas.Restaurant.insert {
      it[name] = restaurant.name ?: ""
      it[address] = restaurant.address ?: ""
      it[picture] = restaurant.picture ?: ""
      it[description] = restaurant.description ?: ""
      it[website] = restaurant.website ?: ""
      it[phone] = restaurant.phone ?: ""
    } get schemas.Restaurant.id
      ?: throw ServiceError("Fail to create new restaurant retry again later")

    return@runQuery schemas.RestaurantUser.insert {
      it[restaurantId] = currentRestaurantId
      it[userId] = currentUserId
    } get schemas.RestaurantUser.id
      ?: throw ServiceError("Fail to create new restaurant user link")
  }
}

fun findRestaurantById(id: Int): Restaurant? {
  return runQuery {
    val foundRestaurants = schemas.Restaurant.select {
      schemas.Restaurant.id eq id
    }

    if (foundRestaurants.count() <= 0)
      return@runQuery null

    val foundRestaurant = foundRestaurants.first()

    return@runQuery Restaurant(
      foundRestaurant[schemas.Restaurant.name],
      foundRestaurant[schemas.Restaurant.address],
      foundRestaurant[schemas.Restaurant.description],
      foundRestaurant[schemas.Restaurant.picture],
      foundRestaurant[schemas.Restaurant.website],
      foundRestaurant[schemas.Restaurant.phone])
  }
}

fun findRestaurantByName(name: String): Restaurant? {
  return runQuery {
    val foundRestaurants = schemas.Restaurant.select {
      schemas.Restaurant.name eq name
    }

    if (foundRestaurants.count() <= 0)
      return@runQuery null

    val foundRestaurant = foundRestaurants.first()

    return@runQuery Restaurant(
      foundRestaurant[schemas.Restaurant.name],
      foundRestaurant[schemas.Restaurant.address],
      foundRestaurant[schemas.Restaurant.description],
      foundRestaurant[schemas.Restaurant.picture],
      foundRestaurant[schemas.Restaurant.website],
      foundRestaurant[schemas.Restaurant.phone])
  }
}

fun updateRestaurantById(currentUserId: Int, currentRestaurantId: Int, restaurant: Restaurant): Int {
  return runQuery {
    val foundUsers = schemas.RestaurantUser.select {
      schemas.RestaurantUser.id eq currentUserId
      schemas.RestaurantUser.id eq currentRestaurantId
    }

    if (foundUsers.count() <= 0)
      throw ServiceError("Cannot find user of the current restaurant")

    val foundRestaurant = foundUsers.first()

    return@runQuery schemas.Restaurant.update({
      schemas.Restaurant.id.eq(currentRestaurantId)
    }) {
      it[name] = restaurant.name ?: foundRestaurant[name]
      it[address] = restaurant.address ?: foundRestaurant[address]
      it[picture] = restaurant.picture?: foundRestaurant[picture]
      it[description] = restaurant.description?: foundRestaurant[description]
      it[website] = restaurant.website ?: foundRestaurant[website]
      it[phone] = restaurant.phone ?: foundRestaurant[phone]
    }
  }
}

fun deleteRestaurantById(userId: Int, restaurantId: Int): Int {
  return runQuery {
    schemas.RestaurantUser.deleteWhere {
      schemas.RestaurantUser.userId eq userId
      schemas.RestaurantUser.restaurantId eq restaurantId
    }

    return@runQuery schemas.Restaurant.deleteWhere {
      schemas.Restaurant.id eq restaurantId
    }
  }
}
