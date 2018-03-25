package controllers

import models.Response
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
  fun getById(@PathParam("id") id: Int): Response {
    println("[trace] Open get by id user")

    val data = findUserById(id)
    if (data == User("", "", "", "")) {
      return Response(false, null, "This user doesn't exist in current database")
0   }

    return Response(true, findUserById(id), "")
  }

  @GET
  @Path("all")
  fun getAll(): Response {
    println("[trace] Open get all user")

    val data = findAllUser()
    if (data.count() <= 0) {
      return Response(false, null, "No one user is currently registered into database")
    }

    return Response(true, findAllUser(), "")
  }

  @GET
  @Path("login")
  fun login(
    @QueryParam("email") email: String,
    @QueryParam("password") password: String): Response {
    println("[trace] Login user")

    val data = userLogin(email, password)
    return Response(data, null, if (data) "" else "Login failed")
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Path("register")
  fun register(user: User): Response {
    println("[trace] Register user")

    val data = userRegister(user)
    return Response(data, null, if (data) "" else "Register failed")
  }
}
