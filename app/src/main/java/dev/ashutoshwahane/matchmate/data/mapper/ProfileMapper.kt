package dev.ashutoshwahane.matchmate.data.mapper

import android.util.Log
import dev.ashutoshwahane.matchmate.data.database.ProfileEntity
import dev.ashutoshwahane.matchmate.data.entity.Profiles
import dev.ashutoshwahane.matchmate.data.entity.Result
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.utils.ProfileStatus

fun Profiles.mapDataToDomain(): List<ProfileModel> {
    return results.map {
        it.mapDataToDomain()
    }
}

fun List<ProfileEntity>.mapDataToDomain(): List<ProfileModel> {
    return map {
        it.mapDataToDomain()
    }
}

fun List<ProfileModel>.mapDataToDB(): List<ProfileEntity> {
    return this.map { it.mapDomainToData() }
}

fun ProfileEntity.mapDataToDomain(): ProfileModel {
    Log.d("debug", "mapDataToDomain: $isAccepted")
    return ProfileModel(
        firstName = firstName,
        lastName = lastName,
        age = age,
        address = address,
        email = email,
        profilePic = profilePic,
        isAccepted = ProfileStatus.valueOf(isAccepted),
    )
}


fun Result.mapDataToDomain(): ProfileModel {
    return ProfileModel(
        firstName = name.first,
        lastName = name.last,
        age = dob.age.toString(),
        address = "${location.street.number} ${location.street.name}, ${location.city}, ${location.state}, ${location.country}",
        email = email,
        profilePic = picture.large,
        isAccepted = ProfileStatus.PENDING,
    )
}

fun ProfileModel.mapDomainToData(): ProfileEntity {
    return ProfileEntity(
        firstName = firstName,
        lastName = lastName,
        age = age,
        address = address,
        isAccepted = isAccepted.name,
        email = email,
        profilePic = profilePic,
    )

}