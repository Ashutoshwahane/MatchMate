package dev.ashutoshwahane.matchmate.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
class ProfileEntity(
    val name: String,
    @PrimaryKey
    val email: String,
    val address: String,
    val age: String,
    val profilePic: String,
)
