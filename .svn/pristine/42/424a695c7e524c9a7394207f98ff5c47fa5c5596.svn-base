package controllers

import models.Restaurant
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("restaurant")
@Produces(APPLICATION_JSON)
class RestaurantController {
  @GET
  fun getById(@QueryParam("id") id: Int): Restaurant {
    return Restaurant("")
  }

  @GET
  @Path("all")
  fun getAll(): Array<Restaurant> {
    return arrayOf(Restaurant(""))
  }
}
