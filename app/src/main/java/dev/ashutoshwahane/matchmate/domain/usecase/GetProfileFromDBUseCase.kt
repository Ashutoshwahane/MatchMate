package dev.ashutoshwahane.matchmate.domain.usecase

import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.repository.ProfileRepository
import dev.ashutoshwahane.matchmate.domain.utils.DataType
import dev.ashutoshwahane.matchmate.domain.utils.ParameterUseCase
import javax.inject.Inject

class GetProfileFromDBUseCase @Inject constructor(
    private val repository: ProfileRepository
) : ParameterUseCase<Unit, List<ProfileModel>>() {


    override suspend fun run(params: Unit, dataType: DataType): List<ProfileModel> {
        return repository.getProfilesFromDB()
    }
}