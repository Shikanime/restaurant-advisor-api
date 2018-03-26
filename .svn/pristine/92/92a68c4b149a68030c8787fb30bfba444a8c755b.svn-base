package services

import generateToken
import models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

const val databaseUrl = "jdbc:postgresql://localhost:5432/resto"
const val databaseDriver = "org.postgresql.Driver"
const val databaseUser = "resto"
const val databasePassword = "evolution"

object UserSchema : Table() {
  val id = integer("id").autoIncrement().primaryKey()
  val firstName = varchar("firstName", length = 20)
  val lastName = varchar("lastName", length = 20)
  val email = varchar("email", length = 50)
  val password = varchar("password", length = 60)
}

fun fromRow(r: ResultRow): User {
  return User(r[UserSchema.firstName],
    r[UserSchema.lastName],
    r[UserSchema.email],
    "")
}

fun findUserById(id: Int): User? {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    val findUsers = UserSchema.select {
      UserSchema.id eq id
    }

    if (findUsers.count() <= 0)
      return@transaction null

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

fun userLogin(email: String, password: String): String? {
  Database.connect(databaseUrl, driver = databaseDriver, user = databaseUser, password = databasePassword)

  return transaction {
    create(UserSchema)

    val findUsers = UserSchema.select {
      UserSchema.email eq email
    }

    if (findUsers.count() <= 0)
      return@transaction null

    val findUser = findUsers.first()

    if (!BCrypt.checkpw(password, findUser[UserSchema.password]))
      return@transaction null

    return@transaction generateToken(findUser[UserSchema.id].toString())
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
