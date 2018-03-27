package controllers

import models.Avis
import models.Response
import models.Restaurant
import models.User
import services.*
import verifyToken
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("restaurant")
@Produces(APPLICATION_JSON)
class RestaurantController {
  @GET
  @Path("{id}")
  @Consumes(APPLICATION_JSON)
  fun getRestaurant(@PathParam("id") id: Int): Response {
    println("[trace] Open get by id restaurant")

    return try {
      Response(true, findRestaurantById(id), "")
    } catch (error: Exception) {
      println("[error] Fail to get restaurant from $id: $error")
      Response(false, null, error.message)
    }
  }

  @GET
  @Path("_name/{name}")
  @Consumes(APPLICATION_JSON)
  fun getRestaurantByName(@PathParam("name") name: String): Response {
    println("[trace] Open get by id restaurant")

    return try {
      Response(true, findRestaurantByName(name), "")
    } catch (error: Exception) {
      println("[error] Fail to get restaurant from $name: $error")
      Response(false, null, error.message)
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
      Response(false, null, error.message)
    }
  }

  @PUT
  @Consumes(APPLICATION_JSON)
  @Path("{id}")
  fun updateRestaurant(@PathParam("id") id: Int, restaurant: Restaurant): Response {
    println("[trace] Update user")

    return try {
      Response(true, updateRestaurantById(id, restaurant), "")
    } catch (error: Exception) {
      println("[error] Fail to login: $error")
      Response(false, null, error.message)
    }
  }

  @GET
  @Path("all")
  fun getAllRestaurant(): Response {
    println("[trace] Open get all restaurant")

    return try {
      Response(true, findAllRestaurant(), "")
    } catch (error: Exception) {
      println("[error] Fail to get all restaurant: $error")
      Response(false, null, error.message)
    }
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/avis")
  fun createAvis(@QueryParam("user_id") userId: Int, @PathParam("id") restaurantId: Int, avis: Avis): Response {
    println("[trace] Register user avis to restaurant")

    return try {
      Response(false, addAvisToRestaurant(userId, restaurantId, avis), "")
    } catch (error: Exception) {
      println("[error] Fail to create new avis to $restaurantId from user $userId: $error")
      Response(false, null, error.message)
    }
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/avis")
  fun deleteAvisByRestaurantId(@HeaderParam("Authorization") bearer: String, @PathParam("id") restaurantId: Int): Response {
    println("[trace] Register user")

    return try {
      Response(true, services.deleteAvisByRestaurantId(verifyToken(bearer.substring(7, -1)).toInt(), restaurantId), "")
    } catch (error: Exception) {
      println("[error] Fail to login: $error")
      Response(false, null, error.message)
    }
  }
}
