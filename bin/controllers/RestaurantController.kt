package controllers

import models.User
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("restaurant")
@Produces(MediaType.APPLICATION_JSON)
class RestaurantController {
    @GET
    fun getById(@QueryParam("id") id: Int): User {
        return User("", "", "", Date())
    }

    @GET
    @Path("all")
    fun getAll(): Array<User> {
        return arrayOf(User("", "", "", Date()))
    }
}
