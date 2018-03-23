package controllers

import models.User
import services.findAllUser
import services.findUserById
import services.userLogin
import services.userRegister
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
class UserController {
    @GET
    fun getById(@QueryParam("id") id: Int): User {
        return findUserById(id)
    }

    @GET
    @Path("all")
    fun getAll(): Array<User> {
        return findAllUser()
    }

    @GET
    @Path("login")
    fun login(@QueryParam("email") email: String, @QueryParam("password") password: String): User {
        return userLogin(email, password)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("register")
    fun register(user: User): User {
        return userRegister(user.email, user.password)
    }
}
