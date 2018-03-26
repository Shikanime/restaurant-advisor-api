package controllers

import models.Avis
import models.Response
import models.Restaurant
import services.addAvisToRestaurant
import services.addRestaurant
import services.findAllRestaurant
import services.findRestaurantById
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("restaurant")
@Produces(APPLICATION_JSON)
class RestaurantController {
  @GET
  fun getById(@QueryParam("id") id: Int): Response {
    println("[trace] Open get by id restaurant")

    return try {
      Response(true, findRestaurantById(id), "")
    } catch (error: Exception) {
      println("[error] Fail to get restaurant from $id: $error")
      Response(false, null, error.toString())
    }
  }

  @GET
  @Path("all")
  fun getAll(): Response {
    println("[trace] Open get all restaurant")

    return try {
      Response(true, findAllRestaurant(), "")
    } catch (error: Exception) {
      println("[error] Fail to get all restaurant: $error")
      Response(false, null, error.toString())
    }
  }

  @POST
  @Consumes(APPLICATION_JSON)
  fun createRestaurant(restaurant: Restaurant): Response {
    println("[trace] Register restaurant")

    return try {
      Response(true, addRestaurant(restaurant), "")
    } catch (error: Exception) {
      println("[error] Register restaurant fail: $error")
      Response(false, null, error.toString())
    }
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Path("avis")
  fun createAvis(@QueryParam("user_id") userId: Int, @QueryParam("restaurant_id") restaurantId: Int, avis: Avis): Response {
    println("[trace] Register user avis to restaurant")

    return try {
      Response(false, addAvisToRestaurant(userId, restaurantId, avis), "")
    } catch (error: Exception) {
      println("[error] Fail to create new avis to $restaurantId from user $userId: $error")
      Response(false, null, error.toString())
    }
  }
}
