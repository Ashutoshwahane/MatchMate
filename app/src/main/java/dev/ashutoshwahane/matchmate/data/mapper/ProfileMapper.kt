package dev.ashutoshwahane.matchmate.data.mapper

import dev.ashutoshwahane.matchmate.data.entity.Profiles
import dev.ashutoshwahane.matchmate.data.entity.Result
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel

fun Profiles.mapDataToDomain(): List<ProfileModel>{
    return results.map {
        it.mapDataToDomain()
    }
}


fun Result.mapDataToDomain(): ProfileModel{
    return ProfileModel(
        firstName = name.first,
        lastName = name.last,
        age = dob.age.toString(),
        address = "${location.street.number} ${location.street.name}, ${location.city}, ${location.state}, ${location.country}",
        email = email,
        profilePic = picture.large

    )
}