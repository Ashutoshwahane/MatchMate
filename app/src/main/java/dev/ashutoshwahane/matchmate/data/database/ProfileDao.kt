package dev.ashutoshwahane.matchmate.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile")
    fun getNotes(): Flow<List<ProfileEntity>>
}

data class Profile(
    val name: String
)