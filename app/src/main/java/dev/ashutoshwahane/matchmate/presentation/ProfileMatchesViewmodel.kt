package dev.ashutoshwahane.matchmate.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.usecase.UpdateProfileUseCase
import dev.ashutoshwahane.matchmate.domain.usecase.GetProfileUseCase
import dev.ashutoshwahane.matchmate.domain.utils.ProfileStatus
import dev.ashutoshwahane.matchmate.domain.utils.UseCaseResponse
import dev.ashutoshwahane.matchmate.utils.DataResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ProfileMatchesUiState(
    val profilesResources: DataResource<List<ProfileModel>> = DataResource.initial(),
)

@HiltViewModel
class ProfileMatchesViewmodel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileMatchesUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchProfiles() {
        getProfileUseCase.invoke(
            scope = viewModelScope,
            callback = object : UseCaseResponse<List<ProfileModel>> {
                override suspend fun onLoading() {
                    super.onLoading()
                    _uiState.update { it.copy(profilesResources = DataResource.loading()) }
                }
            },
            params = Unit,
            dataResourceValue = { resource ->
                Log.d("debug", "fetchProfiles: $resource")
                if (resource.isSuccess()){
                    _uiState.update { it.copy(profilesResources = resource) }
                }
                if (resource.isError()){
                    _uiState.update { it.copy(profilesResources = DataResource.error(null)) }
                }
            }
        )
    }

    fun updateProfileStatus(profile: ProfileModel, newStatus: ProfileStatus) {
        updateProfileUseCase.invoke(
            scope = viewModelScope,
            params = profile.copy(isAccepted = newStatus)
        )
        _uiState.update {
            it.copy(
                profilesResources = it.profilesResources.copy(
                    data = it.profilesResources.data?.map { existingProfile ->
                        if (existingProfile.email == profile.email) {
                            existingProfile.copy(isAccepted = newStatus)
                        } else {
                            existingProfile
                        }
                    }
                )
            )
        }
    }

}