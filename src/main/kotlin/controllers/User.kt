package controllers

import models.User
import services.sendResponse
import services.verifyToken
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("user")
@Produces(APPLICATION_JSON)
class User {
  @GET
  @Path("{id}")
  fun getUserById(@PathParam("id") id: Int): Response {
    return sendResponse("Get user by id") {
      services.findUserById(id)
    }
  }

  @GET
  fun getUser(): Response {
    return sendResponse("Get all user") {
      services.findAllUser()
    }
  }

  @POST
  @Consumes(APPLICATION_JSON)
  fun createUser(user: User): Response {
    return sendResponse("Create new user") {
      services.addUser(user)
    }
  }

  @PUT
  @Consumes(APPLICATION_JSON)
  fun updateUser(@HeaderParam("Authorization") bearer: String,
                 user: User): Response {
    return sendResponse("Update user by id") {
      services.updateUserById(verifyToken(bearer), user)
    }
  }

  @DELETE
  @Consumes(APPLICATION_JSON)
  fun deleteUser(@HeaderParam("Authorization") bearer: String): Response {
    return sendResponse("Delete current user") {
      services.deleteUserById(verifyToken(bearer))
    }
  }

  @GET
  @Path("login")
  fun login(
    @QueryParam("email") email: String,
    @QueryParam("password") password: String): Response {
    return sendResponse("Login user") {
      services.userLogin(email, password)
    }
  }
}
