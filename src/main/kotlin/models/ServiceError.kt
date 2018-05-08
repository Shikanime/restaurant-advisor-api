package models

data class ServiceError(
  override val message: String
) : Exception()
