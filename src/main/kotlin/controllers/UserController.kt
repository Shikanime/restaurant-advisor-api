package controllers

import models.Response
import models.User
import services.*
import verifyToken
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("user")
@Produces(APPLICATION_JSON)
class UserController {
  @GET
  @Path("{id}")
  fun getUser(@PathParam("id") id: Int): Response {
    println("[trace] Open get by id user")

    return try {
      Response(true, findUserById(id), "")
    } catch (error: Exception) {
      println("[error] Fail to get restaurant from $id: $error")
      Response(false, null, error.message)
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
      Response(false, null, error.message)
    }
  }

  @DELETE
  @Consumes(APPLICATION_JSON)
  fun deleteUser(@HeaderParam("Authorization") bearer: String): Response {
    println("[trace] Register user")

    return try {
      Response(true, deleteUserById(verifyToken(bearer.substring(7, bearer.length - 1)).toInt()), "")
    } catch (error: Exception) {
      println("[error] Fail to login: $error")
      Response(false, null, error.message)
    }
  }

  @GET
  @Path("all")
  fun getAllUser(): Response {
    println("[trace] Open get all user")

    return try {
      Response(true, findAllUser(), "")
    } catch (error: Exception) {
      println("[error] Fail to get all users: $error")
      Response(false, null, error.message)
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
      Response(false, null, error.message)
    }
  }
}
