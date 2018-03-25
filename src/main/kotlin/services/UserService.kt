package services

import models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object UserSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val firstName = varchar("firstName", length = 20)
  val lastName = varchar("lastName", length = 20)
  val email = varchar("email", length = 50)
  val password = varchar("password", length = 60)
}

const val databaseUrl = "jdbc:postgresql://localhost:5432/resto"
const val databaseDriver = "org.postgresql.Driver"
const val databaseUser = "resto"
const val databasePassword = "evolution"

fun findUserById(id: Int): User {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    val findUsers= UserSchema.select {
      UserSchema.id eq id
    }

    if (findUsers.count() <= 0)
      return@transaction User("", "", "", "")

    val findUser = findUsers.first()

    return@transaction User(findUser[UserSchema.firstName],
      findUser[UserSchema.lastName],
      findUser[UserSchema.email],
      findUser[UserSchema.password])
  }
}

fun findAllUser(): Array<User> {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    return@transaction UserSchema
      .selectAll()
      .map {
        User(it[UserSchema.firstName],
          it[UserSchema.lastName],
          it[UserSchema.email], "")
      }
      .toTypedArray()
  }
}

fun userLogin(email: String, password: String): Boolean {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    val findUsers = UserSchema.select {
      UserSchema.email eq email
    }

    if (findUsers.count() <= 0)
      return@transaction false

    val findUser = findUsers.first()

    return@transaction BCrypt.checkpw(password, findUser[UserSchema.password])
  }
}

fun userRegister(user: User): Boolean {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    if (UserSchema.select {
        UserSchema.email.eq(user.email)
      }.count() > 0)
      return@transaction false

    UserSchema.insert {
      it[lastName] = user.lastName
      it[firstName] = user.firstName
      it[email] = user.email
      it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt(12));
    } get UserSchema.id ?: return@transaction false

    return@transaction true
  }
}
