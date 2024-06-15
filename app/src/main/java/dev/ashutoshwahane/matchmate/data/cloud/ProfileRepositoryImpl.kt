package dev.ashutoshwahane.matchmate.data.cloud

import dev.ashutoshwahane.matchmate.data.entity.Profiles
import dev.ashutoshwahane.matchmate.data.database.ProfileEntity
import dev.ashutoshwahane.matchmate.data.mapper.mapDataToDomain
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.repository.ProfileRepository
import dev.ashutoshwahane.matchmate.utils.DataResource
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val matchMaker: MatchMateAPI,
): ProfileRepository {
    override suspend fun getProfiles(): List<ProfileModel> {
        return matchMaker.getProfiles().mapDataToDomain()
    }

}