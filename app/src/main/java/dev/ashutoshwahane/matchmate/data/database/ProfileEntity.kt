package dev.ashutoshwahane.matchmate.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
class ProfileEntity(
    val id: Int = 0,
    @PrimaryKey
    val email: String,
    val firstName: String,
    val lastName: String,
    val address: String,
    val age: String,
    val profilePic: String,
    val isAccepted: String
)
