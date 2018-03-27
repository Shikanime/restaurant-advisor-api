package controllers

import models.Response
import services.deleteAvisByAvisId
import verifyToken
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("avis")
@Consumes(APPLICATION_JSON)
class AvisController {
  @DELETE
  @Path("{id}")
  @Consumes(APPLICATION_JSON)
  fun deleteUser(@HeaderParam("Authorization") bearer: String, @PathParam("id") avisId: Int): Response {
    println("[trace] Register user")

    return try {
      Response(true, deleteAvisByAvisId(verifyToken(bearer.substring(7, -1)).toInt(), avisId), "")
    } catch (error: Exception) {
      println("[error] Fail to login: $error")
      Response(false, null, error.message)
    }
  }
}
