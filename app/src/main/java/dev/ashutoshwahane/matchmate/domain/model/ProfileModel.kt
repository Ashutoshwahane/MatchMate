package dev.ashutoshwahane.matchmate.domain.model

import dev.ashutoshwahane.matchmate.domain.utils.ProfileStatus


data class ProfileModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String,
    val age: String,
    var profilePic: String,
    val isAccepted: ProfileStatus,
)
