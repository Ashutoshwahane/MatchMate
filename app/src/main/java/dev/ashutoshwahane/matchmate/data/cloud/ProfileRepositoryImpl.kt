package dev.ashutoshwahane.matchmate.data.cloud

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ashutoshwahane.matchmate.data.database.ProfileDao
import dev.ashutoshwahane.matchmate.data.local.downloadImageAndSave
import dev.ashutoshwahane.matchmate.data.mapper.mapDataToDB
import dev.ashutoshwahane.matchmate.data.mapper.mapDataToDomain
import dev.ashutoshwahane.matchmate.data.mapper.mapDomainToData
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val matchMaker: MatchMateAPI,
    private val profileDao: ProfileDao,
    @ApplicationContext private val context: Context
): ProfileRepository {
    override suspend fun getProfiles(): List<ProfileModel> {
        withContext(Dispatchers.IO) {
            try {
                val profilesFromApi = matchMaker.getProfiles().mapDataToDomain()
                profilesFromApi.forEach {profileModel ->
                    val localImagePath = profileModel.profilePic.let { image ->
                        downloadImageAndSave(context, image, profileModel.email)
                    }
                    Log.d("debug", "getProfiles: $localImagePath")
                    profileModel.profilePic = localImagePath ?: profileModel.profilePic
                }
                profileDao.insertProfiles(profilesFromApi.mapDataToDB())
            }catch (e: Exception){
                e.printStackTrace()
            }
            // Fetch the profiles from the database and return them
        }
        return profileDao.getAllProfiles().mapDataToDomain()
    }

    override suspend fun updateProfile(profileModel: ProfileModel) {
        profileDao.insertProfile(profile = profileModel.mapDomainToData())
    }

    override suspend fun getProfilesFromDB(): List<ProfileModel> {
        return profileDao.getAllProfiles().mapDataToDomain()
    }
}