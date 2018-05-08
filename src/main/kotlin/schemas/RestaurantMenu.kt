package schemas

import org.jetbrains.exposed.sql.Table

object RestaurantMenu : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val menuId = integer("menu_id")
  val restaurantId = integer("restaurant_id")
}
