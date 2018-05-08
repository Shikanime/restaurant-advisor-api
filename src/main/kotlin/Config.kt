val databaseURI = System.getenv("DATABASE_URI")
  ?: "jdbc:postgresql://localhost:5432/resto"

val databaseDriver = System.getenv("DATABASE_DRIVER")
  ?: "org.postgresql.Driver"

val databaseUser = System.getenv("DATABASE_USER")
  ?: "resto"

val databasePassword = System.getenv("DATABASE_PASSWORD")
  ?: "evolution"
