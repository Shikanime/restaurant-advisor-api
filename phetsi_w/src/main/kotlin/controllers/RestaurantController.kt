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

    return Response(true, findRestaurantById(id), "")
  }

  @GET
  @Path("all")
  fun getAll(): Response {
    println("[trace] Open get all restaurant")

    return Response(true, findAllRestaurant(), "")
  }

  @POST
  @Consumes(APPLICATION_JSON)
  fun createRestaurant(restaurant: Restaurant): Response {
    println("[trace] Register restaurant")

    val data = addRestaurant(restaurant)
    return Response(data, null, if (data) "" else "Register failed")
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Path("avis")
  fun createAvis(@QueryParam("user_id") userId: Int, @QueryParam("restaurant_id") restaurantId: Int, avis: Avis): Response {
    println("[trace] Register user avis to restaurant")

    return Response(false, addAvisToRestaurant(userId, restaurantId, avis), "")
  }
}
