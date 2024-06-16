package dev.ashutoshwahane.matchmate.data.repositoryImpl

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ashutoshwahane.matchmate.data.cloud.MatchMateAPI
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
        withContext(Dispatchers.IO) { // Perform network and database operations on a background thread
            try {
                // Fetch profiles from the network API
                val profilesFromApi = matchMaker.getProfiles().mapDataToDomain()

                // Download and save profile images locally
                profilesFromApi.forEach { profileModel ->
                    val localImagePath = profileModel.profilePic.let { image ->
                        downloadImageAndSave(context, image, profileModel.email)
                    }
                    profileModel.profilePic = localImagePath ?: profileModel.profilePic
                }

                // Insert the fetched profiles into the local database
                profileDao.insertProfiles(profilesFromApi.mapDataToDB())
            } catch (e: Exception) {
                e.printStackTrace() // Handle potential exceptions during network/database operations
            }
            // Fetch the profiles from the database and return them (ensures data consistency)
        }
        return profileDao.getAllProfiles().mapDataToDomain()
    }

    override suspend fun updateProfile(profileModel: ProfileModel) {
        // Update a profile in the database (likely inserts or replaces)
        profileDao.insertProfile(profile = profileModel.mapDomainToData())
    }
}