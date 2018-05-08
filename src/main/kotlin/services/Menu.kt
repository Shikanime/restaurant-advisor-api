package services

import models.Menu
import models.ServiceError
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

fun getMenuOfRestaurantByid(currentRestaurantId: Int): Array<Menu> {
  return runQuery {
    findRestaurantById(currentRestaurantId)

    val menuList = schemas.RestaurantMenu.select {
      schemas.RestaurantMenu.restaurantId eq currentRestaurantId
    }

    return@runQuery menuList.map { element ->
      return@map schemas.Menu
        .select {
          schemas.Menu.id eq element[schemas.RestaurantMenu.id]
        }
        .map {
          Menu(it[schemas.Menu.title],
            it[schemas.Menu.description],
            it[schemas.Menu.price])
        }
        .first()
    }
      .toTypedArray()
  }
}

fun addMenuByRestaurantId(currentRestaurantId: Int, menu: Menu): Int? {
  return runQuery {
    findRestaurantById(currentRestaurantId)

    val currentMenuId = schemas.Menu.insert {
      it[title] = menu.title
      it[price] = menu.price
      it[description] = menu.description
    } get schemas.Menu.id
      ?: throw ServiceError("Menu creation failed, retry again")

    return@runQuery schemas.RestaurantMenu.insert {
      it[menuId] = currentMenuId
      it[restaurantId] = currentRestaurantId
    } get schemas.RestaurantMenu.id
      ?: throw ServiceError("Restaurant menu creation failed, retry again")
  }
}

fun deleteMenuByRestaurantId(currentRestaurantId: Int, menuId: Int): Int? {
  return runQuery {
    val foundRestaurants = schemas.RestaurantMenu.select {
      schemas.RestaurantMenu.restaurantId eq currentRestaurantId
    }

    if (foundRestaurants.count() <= 0)
      return@runQuery null

    schemas.RestaurantMenu.deleteWhere {
      schemas.RestaurantMenu.restaurantId eq currentRestaurantId
      schemas.RestaurantMenu.menuId eq menuId
    }

    return@runQuery schemas.Menu.deleteWhere {
      schemas.Menu.id eq foundRestaurants.first()[schemas.RestaurantMenu.menuId]
    }
  }
}
