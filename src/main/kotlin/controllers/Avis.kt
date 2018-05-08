package controllers

import services.sendResponse
import services.verifyToken
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("avis")
@Consumes(APPLICATION_JSON)
class Avis {
  @DELETE
  @Path("{id}")
  @Consumes(APPLICATION_JSON)
  fun deleteAvis(@HeaderParam("Authorization") bearer: String,
                 @PathParam("id") avisId: Int): Response {
    return sendResponse("[trace] Delete avis") {
      services.deleteAvisById(verifyToken(bearer), avisId)
    }
  }
}
