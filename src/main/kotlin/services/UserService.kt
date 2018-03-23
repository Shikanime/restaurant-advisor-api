package services

import models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object UserSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val email = varchar("email", length = 50)
  val password = varchar("password", length = 50)
}

const val databaseUrl = "jdbc:postgresql://localhost:5432/resto"
const val databaseDriver = "org.postgresql.Driver"
const val databaseUser = "resto"
const val databasePassword = "evolution"

fun findUserById(id: Int): User {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  transaction {
    create(UserSchema)
  }

  return User("", "", "", "")
}

fun findAllUser(): Array<User> {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  var allUsers = emptyArray<User>()

  transaction {
    create(UserSchema)

    allUsers = UserSchema
      .selectAll()
      .map {
        User("", "", it[UserSchema.email], it[UserSchema.password])
      }
      .toTypedArray()
  }

  return allUsers
}

fun userLogin(email: String, password: String): User {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  transaction {
    create(UserSchema)
    println("$email, $password")
  }

  return User("", "", "", "")
}

fun userRegister(newEmail: String, newPassword: String): User {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  transaction {
    create(UserSchema)
    UserSchema.select {
      UserSchema.email.eq(newEmail)
    }
    UserSchema.insert {
      it[email] = newEmail
      it[password] = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
    } get UserSchema.id
  }

  return User("", "", newEmail, newPassword)
}
