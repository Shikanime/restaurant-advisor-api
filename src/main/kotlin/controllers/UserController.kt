package controllers

import models.User
import services.findAllUser
import services.findUserById
import services.userLogin
import services.userRegister
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("user")
@Produces(APPLICATION_JSON)
class UserController {
  @GET
  @Path("{id}")
  fun getById(@QueryParam("id") id: Int): User {
    println("[trace] Open get by id user")
    return findUserById(id)
  }

  @GET
  @Path("all")
  fun getAll(): Array<User> {
    println("[trace] Open get all user")
    return findAllUser()
  }

  @GET
  @Path("login")
  fun login(@QueryParam("email") email: String, @QueryParam("password") password: String): User {
    println("[trace] Login user")
    return userLogin(email, password)
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Path("register")
  fun register(user: User): User {
    println("[trace] Register user")
    return userRegister(user.email, user.password)
  }
}
