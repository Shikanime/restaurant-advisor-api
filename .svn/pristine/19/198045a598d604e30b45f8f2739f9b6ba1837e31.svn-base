package controllers

import models.Response
import models.User
import services.addUser
import services.findAllUser
import services.findUserById
import services.userLogin
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON


@Path("user")
@Produces(APPLICATION_JSON)
class UserController {
  @GET
  @Path("{id}")
  fun getById(@PathParam("id") id: Int): Response {
    println("[trace] Open get by id user")

    return try {
      Response(true, findUserById(id), "")
    } catch (error: Exception) {
      println("[error] Fail to get restaurant from $id: $error")
      Response(false, null, error.toString())
    }
  }

  @GET
  @Path("all")
  fun getAll(): Response {
    println("[trace] Open get all user")

    return try {
      Response(true, findAllUser(), "")
    } catch (error: Exception) {
      println("[error] Fail to get all users: $error")
      Response(false, null, error.toString())
    }
  }

  @GET
  @Path("login")
  fun login(
    @QueryParam("email") email: String,
    @QueryParam("password") password: String): Response {
    println("[trace] Login user")

    return try {
      Response(true, userLogin(email, password), "")
    } catch (error: Exception) {
      println("[error] Fail to login: $error")
      Response(false, null, error.toString())
    }
  }

  @POST
  @Consumes(APPLICATION_JSON)
  fun createUser(user: User): Response {
    println("[trace] Register user")

    return try {
      Response(true, addUser(user), "")
    } catch (error: Exception) {
      println("[error] Fail to login: $error")
      Response(false, null, error.toString())
    }
  }
}
