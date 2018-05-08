package schemas

import org.jetbrains.exposed.sql.Table

object RestaurantUser : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val userId = integer("user_id")
  val restaurantId = integer("restaurant_id")
}
