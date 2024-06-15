package dev.ashutoshwahane.matchmate.domain.repository

import dev.ashutoshwahane.matchmate.domain.model.ProfileModel

interface ProfileRepository {
    suspend fun getProfiles(): List<ProfileModel>
    suspend fun getProfilesFromDB(): List<ProfileModel>
    suspend fun updateProfile(profileModel: ProfileModel)
}