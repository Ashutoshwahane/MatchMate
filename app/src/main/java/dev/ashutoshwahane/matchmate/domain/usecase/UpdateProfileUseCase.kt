package dev.ashutoshwahane.matchmate.domain.usecase

import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.repository.ProfileRepository
import dev.ashutoshwahane.matchmate.domain.utils.DataType
import dev.ashutoshwahane.matchmate.domain.utils.ParameterUseCase
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) : ParameterUseCase<ProfileModel, Unit>() {


    override suspend fun run(params: ProfileModel, dataType: DataType): Unit {
        return repository.updateProfile(params)
    }
}