package services

import models.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object UserSchema : Table() {
    val id = varchar("id", 10).autoIncrement().primaryKey()
    val email = varchar("email", length = 50)
    val password = varchar("password", length = 50)
}

fun findUserById(id: Int): User {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
    }

    return User("", "", "", "", Date())
}

fun findAllUser(): Array<User> {
    return arrayOf(User("", "", "", "", Date()))
}

fun userLogin(email: String, password: String): User {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
        println("$email, $password")
    }

    return User("", "", "", "", Date())
}

fun userRegister(newEmail: String, newPassword: String): User {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
        UserSchema.insert {
            it[email] = newEmail
            it[password] = newPassword
        } get UserSchema.id
    }

    return User("", "", newEmail, newPassword, Date())
}
