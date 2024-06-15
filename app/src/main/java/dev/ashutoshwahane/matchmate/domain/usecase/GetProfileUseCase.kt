package dev.ashutoshwahane.matchmate.domain.usecase

import dev.ashutoshwahane.matchmate.data.entity.Profiles
import dev.ashutoshwahane.matchmate.data.database.ProfileEntity
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.repository.ProfileRepository
import dev.ashutoshwahane.matchmate.domain.utils.DataType
import dev.ashutoshwahane.matchmate.domain.utils.ParameterUseCase
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) : ParameterUseCase<Unit, List<ProfileModel>>() {

    override suspend fun run(params: Unit, dataType: DataType): List<ProfileModel> {
        return repository.getProfiles()
    }
}