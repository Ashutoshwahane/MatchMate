package dev.ashutoshwahane.matchmate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ashutoshwahane.matchmate.data.entity.Profiles
import dev.ashutoshwahane.matchmate.data.entity.Result
import dev.ashutoshwahane.matchmate.data.database.ProfileEntity
import dev.ashutoshwahane.matchmate.domain.model.ProfileModel
import dev.ashutoshwahane.matchmate.domain.usecase.GetProfileUseCase
import dev.ashutoshwahane.matchmate.domain.utils.UseCaseResponse
import dev.ashutoshwahane.matchmate.utils.DataResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileMatchesUiState(
    val profilesResources: DataResource<List<ProfileModel>> = DataResource.initial()
)

@HiltViewModel
class ProfileMatchesViewmodel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ProfileMatchesUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchMatches() {
        getProfileUseCase.invoke(
            scope = viewModelScope,
            callback = object : UseCaseResponse<List<ProfileModel>>{
                override suspend fun onLoading() {
                    super.onLoading()
                    _uiState.update {
                        it.copy(profilesResources = DataResource.loading())
                    }
                }
            },
            params = Unit,
            dataResourceValue = {resource ->
                _uiState.update {
                    it.copy(profilesResources = resource)
                }
            }
        )
    }

    fun acceptProfile(profile: ProfileModel) {

    }

    fun denyProfile(profile: ProfileModel){

    }
}