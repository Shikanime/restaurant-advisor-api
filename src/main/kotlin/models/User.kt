package models

import java.util.*

data class User(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val date: Date
)
