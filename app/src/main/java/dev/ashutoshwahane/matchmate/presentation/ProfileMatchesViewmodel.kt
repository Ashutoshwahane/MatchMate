package dev.ashutoshwahane.matchmate.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.usecase.UpdateProfileUseCase
import dev.ashutoshwahane.matchmate.domain.usecase.GetProfileFromDBUseCase
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
    val acceptedProfiles: Set<String> = emptySet(),
    val deniedProfiles: Set<String> = emptySet(),
)

@HiltViewModel
class ProfileMatchesViewmodel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getProfileFromDBUseCase: GetProfileFromDBUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileMatchesUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchMatches() {
        getProfileUseCase.invoke(
            scope = viewModelScope,
            callback = object : UseCaseResponse<List<ProfileModel>> {
                override suspend fun onLoading() {
                    super.onLoading()
                    _uiState.update {
                        it.copy(profilesResources = DataResource.loading())
                    }
                }
            },
            params = Unit,
            dataResourceValue = { resource ->
                Log.d("debug", "fetchMatches: $resource")
                _uiState.update {
                    it.copy(profilesResources = resource)
                }
            }
        )
    }

    fun fetchMatchesFromDB() {
        getProfileFromDBUseCase.invoke(
            scope = viewModelScope,
            callback = object : UseCaseResponse<List<ProfileModel>> {
                override suspend fun onLoading() {
                    super.onLoading()
                    _uiState.update {
                        it.copy(profilesResources = DataResource.loading())
                    }
                }
            },
            params = Unit,
            dataResourceValue = {resource ->
                Log.d("debug", "fetchMatches: $resource")
                _uiState.update {
                    it.copy(profilesResources = resource)
                }
            }

        )
    }



    fun acceptProfile(profile: ProfileModel) {
        updateProfileUseCase.invoke(
            scope = viewModelScope,
            params = profile.copy(isAccepted = ProfileStatus.ACCEPTED),
        )
        _uiState.update {
            it.copy(
                profilesResources = uiState.value.profilesResources.copy(
                    data = uiState.value.profilesResources.data?.map {
                        if (it.email == profile.email) {
                            it.copy(isAccepted = ProfileStatus.ACCEPTED)
                        } else {
                            it
                        }
                    }
                )
            )
        }


    }

    fun denyProfile(profile: ProfileModel) {
        updateProfileUseCase.invoke(
            scope = viewModelScope,
            params = profile.copy(isAccepted = ProfileStatus.DENIED),
        )
        _uiState.update {
            it.copy(
                profilesResources = uiState.value.profilesResources.copy(
                    data = uiState.value.profilesResources.data?.map {
                        if (it.email == profile.email) {
                            it.copy(isAccepted = ProfileStatus.DENIED)
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }
}