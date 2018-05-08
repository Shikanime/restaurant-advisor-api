package models

data class JSONResponse(
  val success: Boolean,
  val data: Any?,
  val error: String?
)
