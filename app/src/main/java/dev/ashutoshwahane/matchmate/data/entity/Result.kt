package dev.ashutoshwahane.matchmate.data.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("cell")
    val cell: String,
    @SerialName("dob")
    val dob: Dob,
    @SerialName("email")
    val email: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Id,
    @SerialName("location")
    val location: Location,
    @SerialName("login")
    val login: Login,
    @SerialName("name")
    val name: Name,
    @SerialName("nat")
    val nat: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("picture")
    val picture: Picture,
    @SerialName("registered")
    val registered: Registered
)