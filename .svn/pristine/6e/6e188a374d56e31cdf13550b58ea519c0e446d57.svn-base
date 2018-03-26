package services

import databaseDriver
import databasePassword
import databaseUrl
import databaseUser
import generateToken
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

fun findUserById(id: Int): User {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    val findUsers = UserSchema.select {
      UserSchema.id eq id
    }

    if (findUsers.count() <= 0)
      throw Exception("Cannot find $id user")

    val findUser = findUsers.first()
    return@transaction User(findUser[UserSchema.firstName],
      findUser[UserSchema.lastName],
      findUser[UserSchema.email],
      "")
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

fun userLogin(email: String, password: String): String {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    val findUsers = UserSchema.select {
      UserSchema.email eq email
    }

    if (findUsers.count() <= 0)
      throw Exception("Cannot find user from email")

    val findUser = findUsers.first()

    if (!BCrypt.checkpw(password, findUser[UserSchema.password]))
      throw Exception("Password doesn't match")

    return@transaction generateToken(findUser[UserSchema.id].toString())
      ?: throw Exception("Generate token fail")
  }
}

fun addUser(user: User): Int {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    if (UserSchema.select {
        UserSchema.email.eq(user.email)
      }.count() > 0)
      throw Exception("Email already exist")

    return@transaction UserSchema.insert {
      it[lastName] = user.lastName
      it[firstName] = user.firstName
      it[email] = user.email
      it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt(12));
    } get UserSchema.id
      ?: throw Exception("Fail to insert user")
  }
}
