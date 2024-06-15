package dev.ashutoshwahane.matchmate.data.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profiles(
    @SerialName("info")
    val info: Info,
    @SerialName("results")
    val results: List<Result>
)