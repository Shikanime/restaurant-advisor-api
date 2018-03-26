package services

import generateToken
import models.User
import org.apache.commons.validator.routines.EmailValidator
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.mindrot.jbcrypt.BCrypt

fun findUserById(id: Int): User {
  return runQuery {
    val findUsers = UserSchema.select {
      UserSchema.id eq id
    }

    if (findUsers.count() <= 0)
      throw Exception("Cannot find $id user")

    val findUser = findUsers.first()
    return@runQuery User(findUser[UserSchema.firstName],
      findUser[UserSchema.lastName],
      findUser[UserSchema.email],
      "")
  }
}

fun findAllUser(): Array<User> {
  return runQuery {
    return@runQuery UserSchema
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
  return runQuery {
    val findUsers = UserSchema.select {
      UserSchema.email eq email
    }

    if (findUsers.count() <= 0)
      throw Exception("Cannot find user from email")

    val findUser = findUsers.first()

    if (!BCrypt.checkpw(password, findUser[UserSchema.password]))
      throw Exception("Password doesn't match")

    return@runQuery generateToken(findUser[UserSchema.id].toString())
  }
}

fun addUser(user: User): Int {
  return runQuery {
    if (UserSchema.select {
        UserSchema.email.eq(user.email)
      }.count() > 0)
      throw Exception("Email already exist")

    if (!EmailValidator.getInstance().isValid(user.email))
      throw Exception("Email is not valid")

    return@runQuery UserSchema.insert {
      it[lastName] = user.lastName
      it[firstName] = user.firstName
      it[email] = user.email
      it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt(12));
    } get UserSchema.id
      ?: throw Exception("Fail to insert user")
  }
}
