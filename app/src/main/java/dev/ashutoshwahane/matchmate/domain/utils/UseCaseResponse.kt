package dev.ashutoshwahane.matchmate.domain.utils

interface UseCaseResponse<Type> {
    suspend fun onLoading() {}
    suspend fun onError(error: String) {}
    suspend fun onSuccess(response: Type) {}
}