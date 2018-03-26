import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import java.security.SecureRandom


fun generateToken(id: String): String? {
  // Generate random 256-bit (32-byte) shared secret
  val random = SecureRandom()
  val sharedSecret = ByteArray(32)
  random.nextBytes(sharedSecret)

  // Create HMAC signer
  val signer = MACSigner(sharedSecret)

  // Prepare JWS object with "Hello, world!" payload
  val jwsObject = JWSObject(JWSHeader(JWSAlgorithm.HS256), Payload(id))

  // Apply the HMAC
  jwsObject.sign(signer)

  // To serialize to compact form, produces something like
  // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
  return jwsObject.serialize()
}

fun verifyToken(token: String): Boolean {
  val jwsObject = JWSObject.parse(token)

  val sharedSecret = ByteArray(32)
  val verifier = MACVerifier(sharedSecret)

  jwsObject.verify(verifier)
  return jwsObject.payload.toString() == "Hello, world!"
}
