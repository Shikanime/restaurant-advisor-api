package services

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import models.JSONResponse
import models.ServiceError
import java.security.SecureRandom
import javax.ws.rs.core.Response

fun generateToken(id: String): String {
  val random = SecureRandom()
  val sharedSecret = ByteArray(32)
  random.nextBytes(sharedSecret)

  val signer = MACSigner(sharedSecret)
  val jwsObject = JWSObject(JWSHeader(JWSAlgorithm.HS256), Payload(id))
  jwsObject.sign(signer)

  return jwsObject.serialize()
}

fun verifyToken(token: String): Int {
  val jwsObject = JWSObject.parse(token.substring(4, token.length - 1))

  val sharedSecret = ByteArray(32)
  val verifier = MACVerifier(sharedSecret)

  if (jwsObject.verify(verifier)) {
    throw ServiceError("Invalid token")
  }

  return jwsObject.payload.toString().toInt()
}

fun sendResponse(message: String, callback: () -> Any?): Response {
  println("[info] $message")

  return try {
    Response
      .status(Response.Status.ACCEPTED)
      .entity(JSONResponse(true, callback(), ""))
      .build()
  } catch (error: ServiceError) {
    Response
      .status(Response.Status.BAD_REQUEST)
      .entity(JSONResponse(false, null, error.message))
      .build()
  } catch (error: Exception) {
    println("[error] Internal error: ${error.message}")
    error.stackTrace.forEach {
      println(it)
    }

    Response
      .status(Response.Status.INTERNAL_SERVER_ERROR)
      .entity(JSONResponse(false, null, "Internal error"))
      .build()
  }
}
