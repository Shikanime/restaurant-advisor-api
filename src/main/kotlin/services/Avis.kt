package services

import models.Avis
import models.ServiceError
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import schemas.RestaurantAvis

fun deleteAvisById(currentUserId: Int, currentAvisId: Int): Int? {
  return runQuery {
    val foundRestaurants = RestaurantAvis.select {
      RestaurantAvis.avisId eq currentAvisId
    }

    if (foundRestaurants.count() <= 0)
      return@runQuery null

    RestaurantAvis.deleteWhere {
      RestaurantAvis.avisId eq currentAvisId
      RestaurantAvis.userId eq currentUserId
    }

    return@runQuery schemas.Avis.deleteWhere {
      schemas.Avis.id eq foundRestaurants.first()[RestaurantAvis.avisId]
    }
  }
}

fun getAvisByRestaurantId(currentRestaurantId: Int): Array<Avis> {
  return runQuery {
    findRestaurantById(currentRestaurantId)

    val avisList = RestaurantAvis.select {
      RestaurantAvis.restaurantId eq currentRestaurantId
    }

    return@runQuery avisList.map { element ->
      return@map schemas.Avis
        .select {
          schemas.Avis.id eq element[RestaurantAvis.id]
        }
        .map {
          Avis(it[schemas.Avis.score], it[schemas.Avis.content])
        }
        .first()
    }
      .toTypedArray()
  }
}

fun addAvisToRestaurant(currentUserId: Int, currentRestaurantId: Int, avis: Avis): Int {
  return runQuery {
    if (avis.score > 5 || avis.score < 0)
      throw ServiceError("Score ${avis.score} must be between 0 or 5")

    findRestaurantById(currentRestaurantId)
    findUserById(currentUserId)

    val currentAvisId = schemas.Avis.insert {
      it[score] = avis.score
      it[content] = avis.content
    } get schemas.Avis.id
      ?: throw ServiceError("Avis creation failed, retry again")

    return@runQuery RestaurantAvis.insert {
      it[userId] = currentUserId
      it[avisId] = currentAvisId
      it[restaurantId] = currentRestaurantId
    } get RestaurantAvis.id
      ?: throw ServiceError("Restaurant avis creation failed, retry again")
  }
}

fun deleteAvisByRestaurantId(currentUserId: Int, currentRestaurantId: Int): Int? {
  return runQuery {
    val foundRestaurants = RestaurantAvis.select {
      RestaurantAvis.restaurantId eq currentRestaurantId
    }

    if (foundRestaurants.count() <= 0)
      return@runQuery null

    RestaurantAvis.deleteWhere {
      RestaurantAvis.restaurantId eq currentRestaurantId
      RestaurantAvis.userId eq currentUserId
    }

    return@runQuery schemas.Avis.deleteWhere {
      schemas.Avis.id eq foundRestaurants.first()[RestaurantAvis.avisId]
    }
  }
}
