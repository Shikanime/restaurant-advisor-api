import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import java.security.SecureRandom


fun generateToken(id: String): String? {
  val random = SecureRandom()
  val sharedSecret = ByteArray(32)
  random.nextBytes(sharedSecret)

  val signer = MACSigner(sharedSecret)
  val jwsObject = JWSObject(JWSHeader(JWSAlgorithm.HS256), Payload(id))
  jwsObject.sign(signer)

  return jwsObject.serialize()
}

fun verifyToken(token: String): Boolean {
  val jwsObject = JWSObject.parse(token)

  val sharedSecret = ByteArray(32)
  val verifier = MACVerifier(sharedSecret)

  return jwsObject.verify(verifier)
}
