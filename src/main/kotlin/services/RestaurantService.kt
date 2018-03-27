package services

import models.Avis
import models.Restaurant
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

fun findRestaurantById(id: Int): Restaurant {
  return runQuery {
    val foundRestaurants = RestaurantSchema.select {
      RestaurantSchema.id eq id
    }

    if (foundRestaurants.count() <= 0)
      throw Exception("Cannot find restaurant")

    return@runQuery Restaurant(foundRestaurants.first()[RestaurantSchema.name])
  }
}

fun findAllRestaurant(): Array<Restaurant> {
  return runQuery {
    return@runQuery RestaurantSchema
      .selectAll()
      .map {
        Restaurant(it[RestaurantSchema.name])
      }
      .toTypedArray()
  }
}

fun addRestaurant(restaurant: Restaurant): Int {
  return runQuery {
    return@runQuery RestaurantSchema.insert {
      it[name] = restaurant.name
    } get RestaurantSchema.id
      ?: throw Exception("Fail to create new restaurant retry again later")
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
