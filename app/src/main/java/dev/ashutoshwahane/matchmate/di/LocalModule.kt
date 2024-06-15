package dev.ashutoshwahane.matchmate.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ashutoshwahane.matchmate.data.database.DatabaseInstance
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DatabaseInstance =
        Room.databaseBuilder(
            context,
            DatabaseInstance::class.java,
            "matchMate.db"
        ).fallbackToDestructiveMigration().build()
}