package controllers

import models.Avis
import models.Menu
import models.Restaurant
import services.addAvisToRestaurant
import services.addMenuByRestaurantId
import services.sendResponse
import services.verifyToken
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("restaurant")
@Produces(APPLICATION_JSON)
class Restaurant {
  @GET
  @Path("{id}")
  @Consumes(APPLICATION_JSON)
  fun getRestaurant(@PathParam("id") id: Int): Response {
    return sendResponse("Open get by id restaurant") {
      services.findRestaurantById(id)
    }
  }

  @GET
  @Path("_name/{name}")
  @Consumes(APPLICATION_JSON)
  fun getRestaurantByName(@PathParam("name") name: String): Response {
    return sendResponse("Get restaurant by id") {
      services.findRestaurantByName(name)
    }
  }

  @POST
  @Consumes(APPLICATION_JSON)
  fun createRestaurant(@HeaderParam("Authorization") bearer: String,
                       restaurant: Restaurant): Response {
    return sendResponse("Create restaurant") {
      services.addRestaurant(verifyToken(bearer), restaurant)
    }
  }

  @PUT
  @Consumes(APPLICATION_JSON)
  @Path("{id}")
  fun updateRestaurant(@HeaderParam("Authorization") bearer: String,
                       @PathParam("id") id: Int, restaurant: Restaurant): Response {
    return sendResponse("Update restaurant") {
      services.updateRestaurantById(verifyToken(bearer), id, restaurant)
    }
  }

  @DELETE
  @Consumes(APPLICATION_JSON)
  @Path("{id}")
  fun deleteRestaurant(@HeaderParam("Authorization") bearer: String,
                       @PathParam("id") id: Int): Response {
    return sendResponse("Delete restaurant") {
      services.deleteRestaurantById(verifyToken(bearer), id)
    }
  }

  @GET
  fun getAllRestaurant(): Response {
    return sendResponse("Get all restaurant") {
      services.getRestaurant()
    }
  }

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/avis")
  fun getAvisByRestaurantId(@PathParam("id") restaurantId: Int): Response {
    return sendResponse("Get avis by restaurant id") {
      services.getAvisByRestaurantId(restaurantId)
    }
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/avis")
  fun createAvisByRestaurantId(@HeaderParam("Authorization") bearer: String,
                               @PathParam("id") restaurantId: Int, avis: Avis): Response {
    return sendResponse("Create avis by restaurant id") {
      addAvisToRestaurant(verifyToken(bearer), restaurantId, avis)
    }
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/avis")
  fun deleteAvisByRestaurantId(@HeaderParam("Authorization") bearer: String,
                               @PathParam("id") restaurantId: Int): Response {
    return sendResponse("[info] Delete avis by restaurant id") {
      services.deleteAvisByRestaurantId(verifyToken(bearer.substring(7, -1)).toInt(), restaurantId)
    }
  }

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/menu")
  fun getMenuByRestaurantId(@PathParam("id") restaurantId: Int): Response {
    return sendResponse("Get menu by restaurant id") {
      services.getMenuOfRestaurantByid(restaurantId)
    }
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/menu")
  fun createMenuByRestaurantId(@PathParam("id") restaurantId: Int, menu: Menu): Response {
    return sendResponse("Create menu by restaurant id") {
      addMenuByRestaurantId(restaurantId, menu)
    }
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{id}/menu")
  fun deleteMenuByRestaurantId(@PathParam("id") restaurantId: Int, menuId: Int): Response {
    return sendResponse("Delete menu by restaurant id") {
      services.deleteMenuByRestaurantId(restaurantId, menuId)
    }
  }
}
