package services

import models.ServiceError
import models.User
import org.apache.commons.validator.routines.EmailValidator
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt

fun findUserById(id: Int): User? {
  return runQuery {
    val findUsers = schemas.User.select {
      schemas.User.id eq id
    }

    if (findUsers.count() <= 0)
      return@runQuery null

    val findUser = findUsers.first()
    return@runQuery User(findUser[schemas.User.firstName],
      findUser[schemas.User.lastName],
      findUser[schemas.User.email],
      "",
      findUser[schemas.User.birthday].toString())
  }
}

fun findAllUser(): Array<User> {
  return runQuery {
    return@runQuery schemas.User
      .selectAll()
      .map {
        User(it[schemas.User.firstName],
          it[schemas.User.lastName],
          it[schemas.User.email],
          "",
          it[schemas.User.birthday].toString())
      }
      .toTypedArray()
  }
}

fun userLogin(email: String, password: String): String? {
  return runQuery {
    val findUsers = schemas.User.select {
      schemas.User.email eq email
    }

    if (findUsers.count() <= 0)
      return@runQuery null

    val findUser = findUsers.first()

    if (!BCrypt.checkpw(password, findUser[schemas.User.password]))
      return@runQuery null

    return@runQuery generateToken(findUser[schemas.User.id].toString())
  }
}

fun addUser(user: User): Int? {
  return runQuery {
    if (schemas.User.select {
        schemas.User.email.eq(user.email!!)
      }.count() > 0)
      return@runQuery null

    when {
      user.firstName == null ->
        throw ServiceError("Empty first name")
      user.lastName == null ->
        throw ServiceError("Empty last name")
      user.email == null ->
        throw ServiceError("Empty email")
      user.password == null ->
        throw ServiceError("Empty password")

      user.firstName.length > 20 ->
        throw ServiceError("Invalid first name length, max is 20 characters")
      user.lastName.length > 20 ->
        throw ServiceError("Invalid last name length, max is 20 characters")
      !EmailValidator.getInstance().isValid(user.email) ->
        throw ServiceError("Email is not valid")
      user.password.length > 60 || user.password.length < 5 ->
        throw ServiceError("Invalid password length, max is 60 characters and minimum is about 6 characters")
    }

    return@runQuery schemas.User.insert {
      it[lastName] = user.lastName ?: ""
      it[firstName] = user.firstName ?: ""
      it[email] = user.email ?: ""
      it[password] = BCrypt.hashpw(user.password, BCrypt.gensalt(12)) ?: ""
      it[birthday] = DateTime.parse(user.birthday)
    } get schemas.User.id
      ?: throw ServiceError("Fail to insert user")
  }
}

fun updateUserById(id: Int, user: User): Int? {
  return runQuery {
    val findUsers = schemas.User.select {
      schemas.User.id.eq(id)
    }

    if (findUsers.count() <= 0)
      return@runQuery null

    val findUser = findUsers.first()

    return@runQuery schemas.User.update({
      schemas.User.id.eq(id)
    }) {
      it[lastName] = user.lastName ?: findUser[lastName]
      it[firstName] = user.firstName ?: findUser[firstName]
      it[email] = user.email ?: findUser[email]
      it[password] = if (user.password != null)
        BCrypt.hashpw(user.password, BCrypt.gensalt(12))
      else
        findUser[password]
      it[birthday] = if (user.birthday != null)
        DateTime.parse(user.birthday)
      else
        findUser[birthday]
    }
  }
}

fun deleteUserById(id: Int): Int {
  return runQuery {
    return@runQuery schemas.User.deleteWhere {
      schemas.User.id.eq(id)
    }
  }
}
