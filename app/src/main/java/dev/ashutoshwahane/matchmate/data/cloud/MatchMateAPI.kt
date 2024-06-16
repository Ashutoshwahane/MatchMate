package dev.ashutoshwahane.matchmate.data.cloud

import dev.ashutoshwahane.matchmate.data.entity.Profiles
import dev.ashutoshwahane.matchmate.data.database.ProfileEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchMateAPI {
    @GET("api/?results=10")
    suspend fun getProfiles(): Profiles
}