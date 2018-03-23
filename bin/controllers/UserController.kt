package controllers

import models.User
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
class UserController {
    @GET
    fun getById(@QueryParam("id") id: Int): User {
        return User("", "", "", Date())
    }

    @GET
    @Path("all")
    fun getAll(): Array<User> {
        return arrayOf(User("", "", "", Date()))
    }

    @GET
    @Path("login")
    fun login(@QueryParam("email") email: String, @QueryParam("password") password: String): User {
        return User("bite", "", "", Date())
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("register")
    fun register(user: User): User {
        return user
    }
}
