package dev.ashutoshwahane.matchmate.domain.repository

import dev.ashutoshwahane.matchmate.data.entity.Profiles
import dev.ashutoshwahane.matchmate.data.database.ProfileEntity
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.utils.DataResource

interface ProfileRepository {
    suspend fun getProfiles(): List<ProfileModel>
}