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
            scope = viewModelScope, // Execute the use case within the ViewModel's coroutine scope
            callback = object : UseCaseResponse<List<ProfileModel>> {
                override suspend fun onLoading() {
                    super.onLoading()
                    // Update UI state to indicate loading is in progress
                    _uiState.update { it.copy(profilesResources = DataResource.loading()) }
                }
            },
            params = Unit,
            dataResourceValue = { resource ->
                Log.d("debug", "fetchProfiles: $resource")
                // Handle the result of the use case
                _uiState.update {
                    it.copy(profilesResources = if (resource.isSuccess() && resource.data.isNullOrEmpty()) {
                        DataResource.error(null) // Handle empty data as an error
                    } else {
                        resource // Directly use the resource if successful or already an error
                    })
                }
            }
        )
    }

    fun updateProfileStatus(profile: ProfileModel, newStatus: ProfileStatus) {
        // Invoke the use case to update the profile status
        updateProfileUseCase.invoke(
            scope = viewModelScope, // Execute within the ViewModel's coroutine scope
            params = profile.copy(isAccepted = newStatus) // Pass the updated profile
        )

        // Update the UI state to reflect the changed profile status
        _uiState.update {
            it.copy(
                profilesResources = it.profilesResources.copy(
                    data = it.profilesResources.data?.map { existingProfile ->
                        if (existingProfile.email == profile.email) {
                            // Update the status of the matching profile
                            existingProfile.copy(isAccepted = newStatus)
                        } else {
                            existingProfile // Keep other profiles unchanged
                        }
                    }
                )
            )
        }
    }

}